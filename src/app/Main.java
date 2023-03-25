package app;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class Main  extends JPanel {
    private static SettingsManager SETTINGS_MGR;
    private static final int FRAME_WIDTH = 725;
    private static final int FRAME_HEIGHT = 485;
    private static JFrame APP_FRAME;
    
    private final JButton JBTN_CHOOSE_FILE = new JButton("📂 Choose File");
    private final JButton JBTN_RESET_ALL = new JButton(" Reset All");
    private final JButton JBTN_RUN = new JButton("Run ≫");
    
    private JFileChooser SELECT_FILE_CHOOSER;
    private JFileChooser SAVE_FILE_CHOOSER;
    
    private final FileNameExtensionFilter PDF_FILE_FILTER = new FileNameExtensionFilter("Pdf file (*.pdf)", "pdf");
    private final FileNameExtensionFilter ZIP_FILE_FILTER = new FileNameExtensionFilter("ZIP File (*.zip)", "zip");
    
    private final JLabel PAGE_COUNT_CAPTION = new JLabel(" Page Count:");
    private final JLabel NO_OF_PAGES_DISPLAY = new JLabel("𝟶");
    
    private final JLabel FILENAME_CAPTION = new JLabel(" File Name:");
    private final JLabel FILENAME_DISPLAY = new JLabel("-");
        
     /* Output Zipped Archive */
    private String destinationFileName = null;
    private ZipOutputStream zipos = null;
    private InputStream fontStream = null;
    private File outputZipArchive = null;
    private ByteArrayOutputStream bos = null;
    private ZipEntry zipEntry = null;
    private File selectedFile = null;
    private String outputFilepath = null;
    /* // Output Zipped Archive */
    
    private final JTextField JTEXT_FIELD_FILEPATH;
    private String absPath = null;
    private JLabel thumbnail = null;
    private ImageIcon imgIcon = null;
    
    private static final HashMap<String,Boolean> FONTNAME_MAPPER  = new HashMap<String,Boolean>();
    private static final JLabel TITLE_DISPLAY = new JLabel("𝙴𝚡𝚝𝚛𝚊𝚌𝚝𝚜 𝙴𝚖𝚋𝚎𝚍𝚍𝚎𝚍 𝙵𝚘𝚗𝚝𝚜 𝙵𝚛𝚘𝚖  𝙿𝙳𝙵");
    private static final JLabel SUBTITLE_DISPLAY = new JLabel(" 𝚁𝚎𝚝𝚞𝚛𝚗𝚜 𝚊𝚕𝚕 𝚏𝚘𝚗𝚝 𝚏𝚒𝚕𝚎𝚜 (.𝚝𝚏𝚏) 𝚒𝚗 𝚊   𝚉𝙸𝙿 𝚊𝚛𝚌𝚑𝚒𝚟𝚎 (.𝚣𝚒𝚙)");
    
    private final HashMap<String,String> UPLOAD_DOC_DETAILS = new HashMap<String,String>();
    private final String[] MONOSPACE_DIGITS = {"𝟶", "𝟷", "𝟸", "𝟹", "𝟺", "𝟻", "𝟼", "𝟽", "𝟾", "𝟿"};
    
    public Main() {
        add(FILENAME_CAPTION);
        add(FILENAME_DISPLAY);
        FILENAME_CAPTION.setFont(SETTINGS_MGR.getBoldFont(12));
        FILENAME_DISPLAY.setFont(SETTINGS_MGR.getPlainFont(12));
        
        add(PAGE_COUNT_CAPTION);
        add(NO_OF_PAGES_DISPLAY);
        PAGE_COUNT_CAPTION.setFont(SETTINGS_MGR.getBoldFont(12));
        NO_OF_PAGES_DISPLAY.setFont(SETTINGS_MGR.getPlainFont(15));
        
        FILENAME_DISPLAY.setHorizontalTextPosition(SwingConstants.LEFT);
        FILENAME_DISPLAY.setHorizontalAlignment(SwingConstants.LEFT);
        
        NO_OF_PAGES_DISPLAY.setHorizontalTextPosition(SwingConstants.LEFT);
        NO_OF_PAGES_DISPLAY.setHorizontalAlignment(SwingConstants.LEFT);
        
        thumbnail = new JLabel();
        thumbnail.setIcon(null);
        thumbnail.setHorizontalTextPosition(SwingConstants.CENTER);
        thumbnail.setHorizontalAlignment(SwingConstants.CENTER);
        thumbnail.setVerticalTextPosition(SwingConstants.CENTER);
        thumbnail.setVerticalAlignment(SwingConstants.CENTER);
        thumbnail.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "𝖯𝗋𝖾𝗏𝗂𝖾𝗐 𝖴𝗉𝗅𝗈𝖺𝖽", TitledBorder.LEFT, TitledBorder.TOP));
        
        add(thumbnail);
        
        TITLE_DISPLAY.setFont(SETTINGS_MGR.getPlainFont(20));
        SUBTITLE_DISPLAY.setFont(SETTINGS_MGR.getPlainFont(12));
        
        add(TITLE_DISPLAY);
        add(SUBTITLE_DISPLAY);
        
        JTEXT_FIELD_FILEPATH = new JTextField("");
        JTEXT_FIELD_FILEPATH.setEditable(false);
        JTEXT_FIELD_FILEPATH.setEnabled(false);
        add(JTEXT_FIELD_FILEPATH);
        
        JTextField[] tfs = {
            JTEXT_FIELD_FILEPATH
        };
        
        for(JTextField tf:tfs) {
            tf.setFont(SETTINGS_MGR.getPlainFont(12));
            tf.setForeground(SETTINGS_MGR.getWhiteColor());
            tf.setDisabledTextColor(SETTINGS_MGR.getGrayColor());
        }
        
        JButton[] mainButtonsArr = {
            JBTN_CHOOSE_FILE, JBTN_RESET_ALL, JBTN_RUN
        };
        for (JButton iconButton:mainButtonsArr) {
            iconButton.setCursor(SETTINGS_MGR.getPointerCursor());
            iconButton.setHorizontalAlignment(SwingConstants.CENTER);
            iconButton.setHorizontalTextPosition(SwingConstants.CENTER);
            iconButton.setVerticalTextPosition(SwingConstants.CENTER);
            iconButton.setHorizontalAlignment(SwingConstants.CENTER);
            add(iconButton);
        }
        JBTN_RUN.setEnabled(false);
        
        int btnHeight=30;
        int btnWidth=130;
        int gapBetweenBtn=5;
        
        int topmostVerticalMargin=10;
        int startBottomBtnXPos=20;
        
        int mainPanelWidth = 255;
        int mainPanelHeight = 430;
        
        int verticalGapBetweenButtonsAndForm = 10;
        
        TITLE_DISPLAY.setBounds(startBottomBtnXPos+2, topmostVerticalMargin+2, (3*btnWidth)+(2*gapBetweenBtn)+(mainPanelWidth-btnWidth), btnHeight+verticalGapBetweenButtonsAndForm);
        SUBTITLE_DISPLAY.setBounds(startBottomBtnXPos+2, topmostVerticalMargin+2+btnHeight+verticalGapBetweenButtonsAndForm, (3*btnWidth)+(2*gapBetweenBtn)+(mainPanelWidth-btnWidth), btnHeight);
        
        JBTN_CHOOSE_FILE.setBounds(startBottomBtnXPos+2, topmostVerticalMargin+2+btnHeight+verticalGapBetweenButtonsAndForm+verticalGapBetweenButtonsAndForm+btnHeight, btnWidth, btnHeight);
        JTEXT_FIELD_FILEPATH.setBounds(startBottomBtnXPos+btnWidth+(3*gapBetweenBtn), topmostVerticalMargin+2+btnHeight+verticalGapBetweenButtonsAndForm+verticalGapBetweenButtonsAndForm+btnHeight, (3*btnWidth)+(2*gapBetweenBtn)+(mainPanelWidth-btnWidth), btnHeight);
        
        
        FILENAME_CAPTION.setBounds(startBottomBtnXPos+2+(2*btnWidth)+(2*gapBetweenBtn), topmostVerticalMargin+2+(3*verticalGapBetweenButtonsAndForm)+(3*btnHeight), btnWidth-30, btnHeight);
        FILENAME_DISPLAY.setBounds(startBottomBtnXPos+2+(2*btnWidth)+(2*gapBetweenBtn)+btnWidth+gapBetweenBtn-30, topmostVerticalMargin+2+(3*verticalGapBetweenButtonsAndForm)+(3*btnHeight), btnWidth+btnWidth, btnHeight);
        
        PAGE_COUNT_CAPTION.setBounds(startBottomBtnXPos+2+(2*btnWidth)+(2*gapBetweenBtn), topmostVerticalMargin+2+(3*verticalGapBetweenButtonsAndForm)+(3*btnHeight), btnWidth-30, btnHeight+btnHeight+verticalGapBetweenButtonsAndForm);
        NO_OF_PAGES_DISPLAY.setBounds(startBottomBtnXPos+2+(2*btnWidth)+(2*gapBetweenBtn)+btnWidth+gapBetweenBtn-30, topmostVerticalMargin+2+(3*verticalGapBetweenButtonsAndForm)+(3*btnHeight), btnWidth+btnWidth, btnHeight+btnHeight+verticalGapBetweenButtonsAndForm);
        
                
        thumbnail.setBounds(startBottomBtnXPos+2, topmostVerticalMargin+2+(3*verticalGapBetweenButtonsAndForm)+(3*btnHeight), (2*btnWidth)+(2*gapBetweenBtn), (8*btnHeight)+(6*verticalGapBetweenButtonsAndForm));
        
        JBTN_RUN.setBounds(startBottomBtnXPos+255+(4*gapBetweenBtn)+btnWidth, topmostVerticalMargin+mainPanelHeight-btnHeight-2, btnWidth, btnHeight);
        JBTN_RESET_ALL.setBounds(startBottomBtnXPos+255+(3*gapBetweenBtn)+2*(gapBetweenBtn+btnWidth), topmostVerticalMargin+mainPanelHeight-btnHeight-2, btnWidth, btnHeight);
        
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLayout(null);
        
        JBTN_CHOOSE_FILE.addActionListener((ActionEvent evt) -> {
            chooseFileAction();
        });
        
        JBTN_RESET_ALL.addActionListener((ActionEvent evt) -> {
            resetAllAction();
        });
        
        JBTN_RUN.addActionListener((ActionEvent evt) -> {
            extractEmbeddedFontsAction();
        });
    }
    
    private String convertToMonospace(String str) {
        String monospaceStr = "";
        String strArr[] = str.split("");
        for (String c : strArr) {
            monospaceStr += MONOSPACE_DIGITS[Integer.parseInt(c)];
        }
        return monospaceStr;
    }
    
    private void chooseFileAction() {
        SwingWorker<Boolean, HashMap<String,String>> worker = new SwingWorker<Boolean, HashMap<String,String>>() {
            @Override
            protected Boolean doInBackground() throws IOException {
                selectedFile = null;
                PDDocument document = null;
                FONTNAME_MAPPER.clear();
                UPLOAD_DOC_DETAILS.clear();
                
                SELECT_FILE_CHOOSER = new JFileChooser();
                SELECT_FILE_CHOOSER.setCurrentDirectory(SETTINGS_MGR.getDefaultDir());
                SELECT_FILE_CHOOSER.setDialogTitle("Choose");

                SELECT_FILE_CHOOSER.setMultiSelectionEnabled(false);
                SELECT_FILE_CHOOSER.setAcceptAllFileFilterUsed(false);
                SELECT_FILE_CHOOSER.addChoosableFileFilter(PDF_FILE_FILTER);

                int result = SELECT_FILE_CHOOSER.showOpenDialog(APP_FRAME);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = SELECT_FILE_CHOOSER.getSelectedFile();
                    
                    absPath = selectedFile.getAbsolutePath();
                    document = Loader.loadPDF(selectedFile);
                    int noOfPages=document.getNumberOfPages();
                    
                    // name | pages | 
                    UPLOAD_DOC_DETAILS.put("name", selectedFile.getName());
                    UPLOAD_DOC_DETAILS.put("pages", (noOfPages+""));
                    publish(UPLOAD_DOC_DETAILS);
                    
                    Splitter splitter = new Splitter();
                    PDDocument indexPage = splitter.split(document).get(0);
                    imgIcon = getThumbnailIcon(indexPage);
                    
                    return true;
                }
                return false;
            }
            
            @Override
            protected void done() { // Can safely update the GUI from this method.
                try {
                    boolean status = get(); // Retrieve the return value of doInBackground.
                    if (status) {
                        JTEXT_FIELD_FILEPATH.setText(absPath);
                        JTEXT_FIELD_FILEPATH.setToolTipText(absPath);
                        
                        SETTINGS_MGR.setDefaultDir(SELECT_FILE_CHOOSER.getCurrentDirectory());
                        
                        String message = " 𝗨𝗣𝗟𝗢𝗔𝗗 𝗦𝗨𝗖𝗖𝗘𝗦𝗦❗"
                        + "\n"
                        + ""
                        + "\n"
                        + "𝖯𝗋𝗈𝖼𝖾𝗌𝗌 𝗂𝗌 𝗈𝗇 𝗌𝗍𝖺𝗇𝖽𝖻𝗒 𝖺𝗇𝖽 𝗋𝖾𝖺𝖽𝗒 𝗍𝗈 𝗋𝗎𝗇."
                        + "\n";
                        JOptionPane.showMessageDialog(APP_FRAME, message, " PDF Data Input Status", JOptionPane.PLAIN_MESSAGE);
                        JBTN_RUN.setEnabled(true);
                        thumbnail.setIcon(imgIcon);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void process(List<HashMap<String,String>> chunks) { // Can safely update the GUI from this method.
                HashMap<String,String> mostRecentValue = chunks.get(chunks.size() - 1);
                
                String filename = mostRecentValue.get("name");
                FILENAME_DISPLAY.setText(filename);
                FILENAME_DISPLAY.setToolTipText(filename);
                
                String pages = mostRecentValue.get("pages");
                int pageCount = Integer.parseInt(pages);
                NO_OF_PAGES_DISPLAY.setText(convertToMonospace(pageCount+""));
                thumbnail.setIcon(imgIcon);
            }
        };
        worker.execute();
    }
    
    private void extractEmbeddedFontsAction() {
        SwingWorker<Boolean, Boolean> worker = new SwingWorker<Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground() throws IOException {
                destinationFileName = null;
                zipos = null;
                fontStream = null;
                outputZipArchive = null;
                bos = null;
                zipEntry = null;
                
                destinationFileName = getCurrentTimeStamp()+"_extractedFonts.zip";
                bos = new ByteArrayOutputStream();
                zipos = new ZipOutputStream(bos);
                
                PDDocument document = Loader.loadPDF(selectedFile);
                for (PDPage page : document.getPages()) {
                    PDResources resources = page.getResources();
                    processResources(resources);
                }
                if(zipos != null) {
                    zipos.close();
                }
                SAVE_FILE_CHOOSER = new JFileChooser();
                SAVE_FILE_CHOOSER.setDialogType(JFileChooser.SAVE_DIALOG);
                SAVE_FILE_CHOOSER.setCurrentDirectory(SETTINGS_MGR.getDefaultDir());
                SAVE_FILE_CHOOSER.setDialogTitle("Save");

                SAVE_FILE_CHOOSER.setMultiSelectionEnabled(false);
                SAVE_FILE_CHOOSER.setAcceptAllFileFilterUsed(false);
                
                SAVE_FILE_CHOOSER.addChoosableFileFilter(ZIP_FILE_FILTER);
                SAVE_FILE_CHOOSER.setSelectedFile(new File(destinationFileName));

                int option = SAVE_FILE_CHOOSER.showSaveDialog(APP_FRAME);
                if (option == JFileChooser.APPROVE_OPTION) {
                    outputZipArchive = SAVE_FILE_CHOOSER.getSelectedFile();
                    return true;
                }
                return false;
            }

            @Override
            protected void done() { // Can safely update the GUI from this method.
                try {
                    boolean status = get(); // Retrieve the return value of doInBackground.
                    if (status) {
                        SETTINGS_MGR.setDefaultDir(SAVE_FILE_CHOOSER.getCurrentDirectory());
                        outputFilepath = outputZipArchive.getAbsolutePath();
                        if(bos != null) {
                            File resultFile=null;
                            FileOutputStream fos=null;
                            
                            InputStream is = new ByteArrayInputStream(bos.toByteArray());
                            resultFile=new File(outputFilepath);
                            fos = new FileOutputStream(resultFile); 
                            int read;
                            byte[] bytes = new byte[1024];
                            while ((read = is.read(bytes)) != -1) {
                                fos.write(bytes, 0, read);
                            }
                            fos.close();
                            
                            System.out.println("Output has been saved at: "+ outputFilepath);
                            Desktop.getDesktop().open(outputZipArchive);
                        }
                    } else {
                        System.out.println("Cancellation/Error.");
                    }
                } catch (InterruptedException | ExecutionException | IOException ex) {
                   ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    public static void main(String[] args) {
        try {
            SETTINGS_MGR = new SettingsManager();
            UIManager.setLookAndFeel(new FlatLightLaf()); // new FlatDarkLaf()

            UIManager.put("Component.arrowType", "triangle");
            UIManager.put("ScrollBar.showButtons", true);
            UIManager.put("ScrollPane.foreground", SETTINGS_MGR.getGrayDarkColor());
            UIManager.put("ScrollPane.font", SETTINGS_MGR.getPlainFont(12));
            UIManager.put("ScrollPane.border", SETTINGS_MGR.getPanelPaddingBorder());

            UIManager.put("Button.foreground", SETTINGS_MGR.getGrayDarkColor());
            UIManager.put("Button.font", SETTINGS_MGR.getPlainFont(12));
            UIManager.put("Button.arc", 5);

            UIManager.put("Label.foreground", SETTINGS_MGR.getDarkColor());
            UIManager.put("Label.font", SETTINGS_MGR.getBoldFont(12));

            UIManager.put("OptionPane.foreground", SETTINGS_MGR.getGrayDarkColor());
            UIManager.put("OptionPane.messageFont", SETTINGS_MGR.getPlainFont(12));

            UIManager.put("TextField.foreground", SETTINGS_MGR.getGrayDarkColor());
            UIManager.put("TextField.caretForeground", SETTINGS_MGR.getLightColor());

            UIManager.put("Spinner.buttonArrowColor", SETTINGS_MGR.getGrayDarkColor());

            ToolTipManager.sharedInstance().setInitialDelay(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> { // Event dispatch thread For GUI code (asynchronously)
            APP_FRAME = new JFrame(" PDF Font Extractor  "); 
            APP_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            APP_FRAME.setSize(FRAME_WIDTH, FRAME_HEIGHT);

            APP_FRAME.getRootPane().putClientProperty("JRootPane.titleBarBackground", SETTINGS_MGR.getLightColor());
            APP_FRAME.getRootPane().putClientProperty("JRootPane.titleBarForeground", SETTINGS_MGR.getGrayDarkColor());
            
            try {
                byte[] fileBytes = Base64.getDecoder().decode(SETTINGS_MGR.getAppIconURI());
                Image img = ImageIO.read(new ByteArrayInputStream(fileBytes));
                ImageIcon imgIcon = new ImageIcon(img);
                APP_FRAME.setIconImage(imgIcon.getImage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            Container appContentPane = APP_FRAME.getContentPane();
            appContentPane.add(new Main());
            
            APP_FRAME.setResizable(false);
            APP_FRAME.setLocationRelativeTo(null);
            APP_FRAME.setVisible(true);
        });
    }
    
    
     // Util functions
    private String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
        Date date = new Date();
        String timestamp = sdf.format(date);

        return timestamp;
    }
    
    private ImageIcon getThumbnailIcon(PDDocument document) throws IOException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        
        Image tempPageImg = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
        ImageIcon tempPageImgIcon = new ImageIcon(tempPageImg);
        ImageIcon tempScaledPageImgIcon = getScaledImageIcon(tempPageImgIcon, 250, 250);
        
        return tempScaledPageImgIcon;
    }
    
    private ImageIcon getScaledImageIcon(ImageIcon imgIcon, int maxImgWidth, int maxImgHeight) {
        ImageIcon icon = null;
        try {
            int iconWidth = imgIcon.getIconWidth();
            int iconHeight = imgIcon.getIconHeight();

            Image tmpImage = imgIcon.getImage();
            BufferedImage bImg = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_RGB);
            bImg.getGraphics().drawImage(tmpImage, 0, 0, null);
            tmpImage.flush();

            int iconLength = iconWidth;
            int maxImgLength = maxImgWidth;
            if (iconHeight > iconWidth) {
                iconLength = iconHeight;
                maxImgLength = maxImgHeight;
            }
            double ratio = (maxImgLength * 1.0) / (iconLength * 1.0);
            long newWidth = (long) (Math.round(ratio * iconWidth));
            long newHeight = (long) (Math.round(ratio * iconHeight));

            Image outputImg = bImg.getScaledInstance((int) newWidth, (int) newHeight, Image.SCALE_SMOOTH);
            icon = new ImageIcon(outputImg);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return icon;
    }
    
    private void processResources(PDResources resources) throws IOException {
        if (resources == null) {
            return;
        }
        for (COSName key : resources.getFontNames()) {
            PDFontDescriptor fd = null;
            String name = null;
            PDFont font = resources.getFont(key);
            if (font instanceof PDTrueTypeFont) {
                fd = font.getFontDescriptor();
                name = font.getName();
            } else if (font instanceof PDType0Font) {
                PDCIDFont descendantFont = ((PDType0Font) font).getDescendantFont();
                if (descendantFont instanceof PDCIDFontType2) {
                    fd = descendantFont.getFontDescriptor();
                    name = font.getName();
                }
            }
            if (fd != null) {
                String fontFilename = name + ".tff";
                PDStream ff2Stream = fd.getFontFile2();
                if (ff2Stream != null && !FONTNAME_MAPPER.containsKey(fontFilename)) {
                    byte[] buffer = ff2Stream.toByteArray();
                    fontStream = new ByteArrayInputStream(buffer);

                    zipEntry = new ZipEntry(fontFilename);
                    zipos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = fontStream.read(bytes)) >= 0) {
                        zipos.write(bytes, 0, len);
                    }
                    zipos.closeEntry();
                    FONTNAME_MAPPER.put(fontFilename, true);
                }
            }
        } // end for-loop
        for (COSName name : resources.getXObjectNames()) {
            PDXObject xobject = resources.getXObject(name);
            if (xobject instanceof PDFormXObject) {
                PDFormXObject xObjectForm = (PDFormXObject) xobject;
                PDResources formResources = xObjectForm.getResources();
                processResources(formResources);
            }
        }
    }
    
    private void resetAllAction() {
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() {
                destinationFileName = null;
                zipos = null;
                fontStream = null;
                outputZipArchive = null;
                bos = null;
                absPath=null;
                zipEntry = null;
                
                FONTNAME_MAPPER.clear();
                UPLOAD_DOC_DETAILS.clear();
                
                return true;
            }

            @Override
            protected void done() { // Can safely update the GUI from this method.
                try {
                    boolean status = get(); // Retrieve the return value of doInBackground.
                    if (status) {
                        NO_OF_PAGES_DISPLAY.setText("𝟶");
                        FILENAME_DISPLAY.setText("-");
                        FILENAME_DISPLAY.setToolTipText("");
                        JBTN_RUN.setEnabled(false);
                        thumbnail.setIcon(null);
                        
                        JTEXT_FIELD_FILEPATH.setText("");
                        JTEXT_FIELD_FILEPATH.setToolTipText("");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}
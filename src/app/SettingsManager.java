package app;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

public class SettingsManager {
    private static final String APP_ICON_URI = "iVBORw0KGgoAAAANSUhEUgAAAGAAAABgCAYAAADimHc4AAAAAXNSR0IArs4c6QAABDlJREFUeF7tnUtoE1EUhv/JpGnTprVFSQ2tj/oAq8GFqGB1Ux8LoYIiCrpx4cJNK/iAguDGorix6E5dqYiroogbBbtTSzei6EJwISJdKUJFa+0rMrFppzHFQybHeyf9B0IgnHvume+bM3dmc+OAh1ECjtHZOTkowPBFQAEUYJiA4enZARRgmIDh6dkBFGCYgOHp2QEUYJiA4enZARRgmIDh6U10wGoAKQBRw+cedPqXAL4FTfI/BFQDODr9aQ9asEXj2wAMBK1HW8BZAN0AlgQt1MLxVgvwrvqHAHZbCK5UJe0A8DxoMq0OeAZge9DiLB9vbQfcAnCsELwNbgxptxKNjotYJGI539nyXk2M4un4SH69VnbAfgAP8itNuzF0VdUjGfnz4JNyXKyMVoZGwO3RYXSPfA6FgE8Amv2V7ozG0RVvmFN8mQiw7hZ0EECfn3RLJIremuRfV3qZCLDuFnQPwBE/7Z74YqQL3GooYJZSKZ+C3gNYk0uddFzcSDQWvM9TgI4A77W8NpfaW3h7qgu/f1GAjoCM/3JfAAKsW4QXmgDrFmEKKOLNppSLMAVQQBEEBEPC8ibMDhDIzA/hLUgAjR0ggKQZQgGadAW5KUAASTOEAjTpCnJTgACSZggFaNIV5KYAASTNEArQpCvITQECSJohFKBJV5CbAgSQNEMoQJOuIDcFCCBphlCAJl1BbgoQQNIMoQBNuoLcFCCApBlCAZp0BbkpQABJM4QCNOkKclOAAJJmCAVo0hXkpgABJM0QCtCkK8hNAQJImiEUoElXkJsCBJA0QyhAk64gNwUIIGmGUIAmXUFuChBA0gyhAE26gtwUIICkGUIBmnQFuSlAAEkzhAI06QpyU4AAkmYIBWjSFeSmAAEkzZCyElCbbkWyrh4p186Nc0enMvg6MAjHcTA0NYGfmQwej33HzV/D+Y7DtVmHB771yiV437Yf/U3rsiWeH/mCt5Nj85UbHgHxZc3Y8uQ+KhbV2c4+W1/ZCVh/9TJSh7wdLcNxlJ2AXUPvwkF+ukoKMKxLKMC6Lcs+AlieY+ffM65tsB/x5ibDWOXTCwVYtwgPAtjqP827iaWocSJoOd2JVWc65QQMR4ZVQC+AU352xyvr0BFLZH/a1HcHDdvm+DGMef7pwypgI4DX/tOqgZPdO7TFrcj+nDp8IPuxXYRQgHVrgMf4EYAOvwRv/+iuqoYZCdZe9gUKC9uLmHcKaQBv8s/F64R9sQTaK+IzW9iHQcQ/BFjZAR7XkwCuzQfY6whvYQ7D8WFyHD8wZy9Cf9nWPQX5i7sO4EQYIAeo0fuLlhcBxmeHlnLXxPxazgG4GLRAi8db3QE5bpsBXACw12KQxZYWCgG5k1s7/UduewCsyP+bk2IJGB5n7SJsmEu4ptdcA8JFwlC1FGAIfG5aCqAAwwQMT88OoADDBAxPzw4wLOA3KptIf7A4xLEAAAAASUVORK5CYII=";
    
    private static final Color WHITE = new Color(255, 255, 255); // #ffffff | White
    private static final Color LIGHT = new Color(248, 249, 250); // #f8f9fa | Light
    
    private static final Color GRAY = new Color(108, 117, 125); // #6c757d | Gray
    private static final Color MEDIUM_GRAY = new Color(85, 85, 85); // #555555 | Medium Gray
    private static final Color GRAY_DARK = new Color(52, 58, 64); // #343a40 | Gray-Dark
    private static final Color DARK = new Color(13,13,14); // #343a40 | Dark
    
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
    private static final Border PANEL_PADDING_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);
    private static final Border RAISED_PANEL_MEDIUM_GRAY_BORDER = BorderFactory.createBevelBorder(BevelBorder.RAISED, MEDIUM_GRAY, MEDIUM_GRAY,MEDIUM_GRAY,MEDIUM_GRAY);
    private static final Border LOWERED_PANEL_GRAY_DARK_BORDER = new SoftBevelBorder(BevelBorder.LOWERED, GRAY_DARK, GRAY_DARK, GRAY_DARK, GRAY_DARK);
    private static final Border LOWERED_PANEL_GRAY_BORDER = new SoftBevelBorder(BevelBorder.LOWERED, GRAY, GRAY, GRAY, GRAY);
    
    private static final Font Consolas = new Font("Consolas", Font.PLAIN, 11);
    
    private final Cursor POINTER_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private File DEFAULT_DIR = new File(System.getProperty("user.home"));
    
    public void setDefaultDir(File defaultDir) {
        this.DEFAULT_DIR=defaultDir;
    }
    public File getDefaultDir() {
        return DEFAULT_DIR;
    }
    
    public String getAppIconURI() {
        return APP_ICON_URI;
    }
    
    public Color getWhiteColor() {
        return WHITE;
    }
    public Color getLightColor() {
        return LIGHT;
    }
    public Color getGrayColor() {
        return GRAY;
    }
    public Color getMediumGrayColor() {
        return MEDIUM_GRAY;
    }
    public Color getGrayDarkColor() {
        return GRAY_DARK;
    }
    public Color getDarkColor() {
        return DARK;
    }
    
    public Border getEmptyBorder() {
        return EMPTY_BORDER;
    }
    public Border getPanelPaddingBorder() {
        return PANEL_PADDING_BORDER;
    }
    public Border getRaisedMediumGrayBorder() {
        return RAISED_PANEL_MEDIUM_GRAY_BORDER;
    }
    
    public Border getLoweredPanelGrayDarkBorder() {
        return LOWERED_PANEL_GRAY_DARK_BORDER;
    }
    public Border getLoweredPanelGrayBorder() {
        return LOWERED_PANEL_GRAY_BORDER;
    }
    
    
    public Font getConsolasFont() {
        return Consolas;
    }
    public Font getPlainFont(int size) {
//        return new Font("Segoe UI Variable", Font.PLAIN, size);
        return new Font("Arial Nova Light", Font.PLAIN, size);
    }
    public Font getBoldFont(int size) {
        return new Font("Arial Unicode MS", Font.BOLD, size);
    }
    public Font getItalicFont(int size) {
        return new Font("Segoe UI Variable", Font.ITALIC, size);
    }
    public Font getType1Font(int size) {
        return new Font("Arial Unicode MS", Font.TYPE1_FONT, size);
    }
    public Font getTrueTypeFont(int size) {
        return new Font("Segoe UI Variable", Font.TRUETYPE_FONT, size);
    }
    public Font getMonospacedFont(int size) {
        return new Font(Font.MONOSPACED, Font.PLAIN, size);
    }
    public Cursor getPointerCursor() {
        return POINTER_CURSOR;
    }
}
// iconFont = new Font("Arial Unicode MS", Font.ROMAN_BASELINE, iconFontSize);
//        textFont = new Font("Arial Nova Light", Font.ROMAN_BASELINE, textFontSize);
//        placeholderFont = new Font("Segoe UI Emoji", Font.ROMAN_BASELINE, placeholderFontSize);
//        footerFont = new Font("Arial Unicode MS", Font.ROMAN_BASELINE, footerFontSize);
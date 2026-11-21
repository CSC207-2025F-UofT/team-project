package main.view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public final class ViewUtils {

    private static final int screenWidth;
    private static final int screenHeight;

    // runs when class is first loaded
    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;

        registerFont("/Figtree_Font/Figtree-Black.tff");
        registerFont("/Figtree_Font/Figtree-Medium.tff");
        registerFont("/Figtree_Font/Figtree-SemiBold");
    }

    private ViewUtils() {}

    public static int getScreenWidth() { return screenWidth; }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setRelativeBounds(Component comp, double xPct, double yPct,
                                         double widthPct, double heightPct) {
        int x = (int) (getScreenWidth() * xPct);
        int y = (int) (getScreenHeight() * yPct);
        int width = (int) (getScreenWidth() * widthPct);
        int height = (int) (getScreenHeight() * heightPct);
        comp.setBounds(x, y, width, height);
    }

    public static void setFont(Component comp, String font, int style, int minSize, double relativeSize) {
        int relSize = (int) (screenHeight * relativeSize);
        comp.setFont(new Font(font, style, Math.max(minSize, relSize)));
    }

    private static void registerFont(String filePath){
        try (InputStream is = ViewUtils.class.getResourceAsStream(filePath);) {
            if (is == null) {
                return;
            }
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}

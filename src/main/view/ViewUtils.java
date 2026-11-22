package main.view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

public final class ViewUtils {

    private static final int screenWidth;
    private static final int screenHeight;

    // runs when class is first loaded
    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;

        registerFont("/Figtree_Font/Figtree-Black.ttf");
        registerFont("/Figtree_Font/Figtree-Medium.ttf");
        registerFont("/Figtree_Font/Figtree-SemiBold.ttf");
        registerFont("/Figtree Font/Figtree ExtraBold");
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

    private static ImageIcon loadImage(String filePath) {
        URL url = ViewUtils.class.getResource(filePath);
        if (url == null) {
            return null;
        }
        return new ImageIcon(url);
    }

    public static JLabel createImage(String filePath, double xPct, double yPct, double widthPct) {
        ImageIcon image = loadImage(filePath);
        if (image == null || image.getIconWidth() <= 0) {
            return new JLabel();
        }

        int x = (int) (xPct * getScreenWidth());
        int y = (int) (yPct * getScreenHeight());
        int width = (int) Math.round(widthPct * getScreenWidth());
        int height = (int) Math.round(width * ((double) image.getIconHeight() / image.getIconWidth()));

        Image scaledImage = image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setBounds(x, y, width, height);
        return label;
    }

    public static void setRelativeSquareBounds(Component comp, double xPct, double yPct, double sizePct) {
        int x = (int) (getScreenWidth() * xPct);
        int y = (int) (getScreenHeight() * yPct);
        int size = (int) (getScreenHeight() * sizePct);
        comp.setBounds(x, y, size, size);
    }

    public static JButton createImageButton(String filePath, double xPct, double yPct, double widthPct) {
        ImageIcon image = loadImage(filePath);
        if (image == null || image.getIconWidth() <= 0) {
            return new JButton();
        }

        int x = (int) (xPct * getScreenWidth());
        int y = (int) (yPct * getScreenHeight());
        int width = (int) Math.round(widthPct * getScreenWidth());
        int height = (int) Math.round(width * ((double) image.getIconHeight() / image.getIconWidth()));

        Image scaledImage = image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        JButton button = new JButton();
        button.setIcon(new ImageIcon(scaledImage));
        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        return button;
    }

    public static void setRelativeLocation(Component comp, double xPct, double yPct) {
        int x = (int) (xPct * getScreenWidth());
        int y = (int) (yPct * getScreenHeight());
        comp.setLocation(x, y);
    }
}

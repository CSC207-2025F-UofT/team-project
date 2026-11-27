package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * A panel that draws the UNO background image, stretched to fill the panel.
 * If the image can't be found, it falls back to a painted gradient.
 */
public class BackgroundPanel extends JPanel {

    private final BufferedImage image;

    public BackgroundPanel() {
        image = loadImage("/backgrounds/login_background.png");
    }

    public BackgroundPanel(String imagePath) {
        image = loadImage(imagePath);
    }

    /** Loads an image from src/main/resources, or returns null if it's missing. */
    private static BufferedImage loadImage(String path) {
        URL url = BackgroundPanel.class.getResource(path);
        if (url == null) {
            System.err.println("Background image not found: " + path);
            return null;
        }
        try {
            return ImageIO.read(url);
        }
        catch (IOException e) {
            System.err.println("Could not read background image: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        if (image != null) {
            // Smooth scaling so the enlarged image doesn't look blocky
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            // Scale so the image covers the whole panel (cropping a little if needed)
            double scale = Math.max((double) w / image.getWidth(), (double) h / image.getHeight());
            int imgW = (int) Math.ceil(image.getWidth() * scale);
            int imgH = (int) Math.ceil(image.getHeight() * scale);
            g2.drawImage(image, (w - imgW) / 2, (h - imgH) / 2, imgW, imgH, null);
        }
        else {
            // Fallback: dark on the left, deep red on the right
            g2.setPaint(new GradientPaint(0, 0, new Color(20, 3, 5), w, 0, new Color(122, 7, 10)));
            g2.fillRect(0, 0, w, h);
        }
    }
}
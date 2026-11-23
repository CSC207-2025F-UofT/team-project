package data_access;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import javax.imageio.ImageIO;

import entity.City;
import use_case.shared.ImageDataAccessInterface;

/**
 * Fake image source: returns the same grey picture every time.
 * AppBuilder uses it when MAPBOX_ACCESS_TOKEN isn't set, so the game can be clicked through without a key.
 */
public class PlaceholderImageDataAccessObject implements ImageDataAccessInterface {
    private final byte[] image;

    public PlaceholderImageDataAccessObject(int imageSize) {
        this.image = drawPlaceholder(imageSize);
    }

    @Override
    public byte[] getImage(City city, int zoomStep) {
        return image.clone();
    }

    private static byte[] drawPlaceholder(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(70, 80, 70));
        g.fillRect(0, 0, size, size);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(12, size / 20)));
        String text = "Placeholder image (no Mapbox token)";
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (size - textWidth) / 2, size / 2);
        g.dispose();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

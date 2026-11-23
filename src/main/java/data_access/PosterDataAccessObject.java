package data_access;

import user_case.MoviePoster.MoviePosterDataAccessInterface;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * Data Access Object for loading movie poster images from TMDB URLs.
 */
public class PosterDataAccessObject implements MoviePosterDataAccessInterface {

    private static final String DEFAULT_PLACEHOLDER_PATH = "/no_poster.png";
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 300;

    @Override
    public BufferedImage fetchPosterImage(String posterUrl) throws IOException {
        URL url = URI.create(posterUrl).toURL();
        return ImageIO.read(url);
    }

    @Override
    public BufferedImage getDefaultPlaceholderImage() {
        try (InputStream is = getClass().getResourceAsStream(DEFAULT_PLACEHOLDER_PATH)) {
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                if (img != null) {
                    return img;
                }
            }
        } catch (IOException ignored) {
            // Fall through to programmatic placeholder generation
        }
        return createFallbackPlaceholder();
    }

    private BufferedImage createFallbackPlaceholder() {
        BufferedImage placeholder = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(new Color(60, 63, 65));
        g2d.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2d.drawString("No Poster Available", 22, DEFAULT_HEIGHT / 2);
        g2d.dispose();
        return placeholder;
    }
}
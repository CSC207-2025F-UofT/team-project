package user_case.MoviePoster;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * DAO interface for fetching movie poster images.
 */
public interface MoviePosterDataAccessInterface {
    /**
     * Retrieves the poster image from the given URL.
     * @param posterUrl the full URL of the poster
     * @return the fetched BufferedImage
     * @throws IOException if the image cannot be retrieved or decoded
     */
    BufferedImage fetchPosterImage(String posterUrl) throws IOException;

    /**
     * Retrieves the default placeholder image when a poster is unavailable.
     * @return the default placeholder BufferedImage
     */
    BufferedImage getDefaultPlaceholderImage();
}
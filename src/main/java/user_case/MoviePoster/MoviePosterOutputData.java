package user_case.MoviePoster;

import java.awt.image.BufferedImage;

/**
 * Output Data for the View Movie Poster Use Case.
 */
public class MoviePosterOutputData {
    private final String movieTitle;
    private final BufferedImage posterImage;
    private final boolean isPlaceholder;

    public MoviePosterOutputData(String movieTitle, BufferedImage posterImage, boolean isPlaceholder) {
        this.movieTitle = movieTitle;
        this.posterImage = posterImage;
        this.isPlaceholder = isPlaceholder;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public BufferedImage getPosterImage() {
        return posterImage;
    }

    public boolean isPlaceholder() {
        return isPlaceholder;
    }
}
package interface_adapter.MoviePoster;

import java.awt.image.BufferedImage;

/**
 * The state for the Movie Poster ViewModel.
 */
public class MoviePosterState {
    private String movieTitle = "";
    private BufferedImage posterImage = null;
    private boolean isPlaceholder = false;

    public MoviePosterState() {}

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public BufferedImage getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(BufferedImage posterImage) {
        this.posterImage = posterImage;
    }

    public boolean isPlaceholder() {
        return isPlaceholder;
    }

    public void setPlaceholder(boolean placeholder) {
        isPlaceholder = placeholder;
    }
}
package user_case.MoviePoster;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Interactor for the View Movie Poster Use Case.
 */
public class MoviePosterInteractor implements MoviePosterInputBoundary {
    private final MoviePosterDataAccessInterface posterDataAccessObject;
    private final MoviePosterOutputBoundary posterPresenter;

    public MoviePosterInteractor(MoviePosterDataAccessInterface posterDataAccessObject,
                                 MoviePosterOutputBoundary posterPresenter) {
        this.posterDataAccessObject = posterDataAccessObject;
        this.posterPresenter = posterPresenter;
    }

    @Override
    public void execute(MoviePosterInputData inputData) {
        String posterUrl = inputData.getPosterPath();
        String title = inputData.getMovieTitle();

        // Extension 2a: Check for missing or invalid TMDB poster URL
        if (isInvalidPosterUrl(posterUrl)) {
            BufferedImage placeholder = posterDataAccessObject.getDefaultPlaceholderImage();
            posterPresenter.preparePosterView(new MoviePosterOutputData(title, placeholder, true));
            return;
        }

        try {
            BufferedImage image = posterDataAccessObject.fetchPosterImage(posterUrl);
            if (image == null) {
                throw new IOException("Decoded image is null");
            }
            posterPresenter.preparePosterView(new MoviePosterOutputData(title, image, false));
        } catch (IOException e) {
            // Extension 2a: Fallback to placeholder if network/decoding fails
            BufferedImage placeholder = posterDataAccessObject.getDefaultPlaceholderImage();
            posterPresenter.preparePosterView(new MoviePosterOutputData(title, placeholder, true));
        }
    }

    private boolean isInvalidPosterUrl(String url) {
        return url == null
                || url.isBlank()
                || url.endsWith("null")
                || url.endsWith("/w500");
    }
}
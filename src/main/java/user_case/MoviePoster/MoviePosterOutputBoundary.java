package user_case.MoviePoster;

/**
 * Output Boundary for the View Movie Poster Use Case.
 */
public interface MoviePosterOutputBoundary {
    /**
     * Prepares the view with the retrieved poster (or fallback placeholder).
     * @param outputData the output data containing the image
     */
    void preparePosterView(MoviePosterOutputData outputData);
}
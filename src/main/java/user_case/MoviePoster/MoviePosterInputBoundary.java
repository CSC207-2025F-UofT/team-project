package user_case.MoviePoster;

/**
 * Input Boundary for actions related to viewing a movie poster.
 */
public interface MoviePosterInputBoundary {
    /**
     * Executes the view movie poster use case.
     * @param inputData the input data containing movie title and poster URL/path
     */
    void execute(MoviePosterInputData inputData);
}
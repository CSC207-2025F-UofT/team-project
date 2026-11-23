package interface_adapter.MoviePoster;

import user_case.MoviePoster.MoviePosterInputBoundary;
import user_case.MoviePoster.MoviePosterInputData;

/**
 * Controller for the View Movie Poster Use Case.
 */
public class MoviePosterController {
    private final MoviePosterInputBoundary posterInteractor;

    public MoviePosterController(MoviePosterInputBoundary posterInteractor) {
        this.posterInteractor = posterInteractor;
    }

    /**
     * Triggers loading and displaying the poster for a given movie.
     * @param movieTitle the title of the movie
     * @param posterPath the full poster URL from Movie.getPosterPath()
     */
    public void execute(String movieTitle, String posterPath) {
        MoviePosterInputData inputData = new MoviePosterInputData(movieTitle, posterPath);
        posterInteractor.execute(inputData);
    }
}
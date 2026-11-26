package interface_adapter.MovieDetail;

import user_case.MovieDetail.MovieDetailInputBoundary;
import user_case.MovieDetail.MovieDetailInputData;

/**
 * The controller for the Movie Detail Use Case.
 */
public class MovieDetailController {

    private final MovieDetailInputBoundary movieDetailInteractor;

    public MovieDetailController(MovieDetailInputBoundary movieDetailInteractor) {
        this.movieDetailInteractor = movieDetailInteractor;
    }

    /**
     * Executes the Movie Detail Use Case.
     *
     * @param movieID the ID of the movie to view details for
     */
    public void execute(int movieID) {
        final MovieDetailInputData movieDetailInputData = new MovieDetailInputData(movieID);
        movieDetailInteractor.execute(movieDetailInputData);
    }
}
package user_case.MovieDetail;

import entity.Movie;

public class MovieDetailInteractor implements MovieDetailInputBoundary {

    private final MovieDetailDataAccessInterface movieDetailDataAccess;
    private final MovieDetailOutputBoundary movieDetailPresenter;

    public MovieDetailInteractor(MovieDetailDataAccessInterface movieDetailDataAccess,
                                 MovieDetailOutputBoundary movieDetailPresenter) {
        this.movieDetailDataAccess = movieDetailDataAccess;
        this.movieDetailPresenter = movieDetailPresenter;
    }

    @Override
    public void execute(MovieDetailInputData movieDetailInputData) {
        int movieID = movieDetailInputData.getMovieID();
        Movie movie = movieDetailDataAccess.getMovieById(movieID);

        if (movie == null) {
            movieDetailPresenter.prepareFailView("Movie not found for ID: " + movieID);
            return;
        }

        MovieDetailOutputData outputData = new MovieDetailOutputData(
                movieID,
                movie.getName(),
                movie.getDescription(),
                movie.getRating(),
                movie.getReleaseDate(),
                movie.getPosterPath()
        );
        movieDetailPresenter.prepareSuccessView(outputData);
    }
}
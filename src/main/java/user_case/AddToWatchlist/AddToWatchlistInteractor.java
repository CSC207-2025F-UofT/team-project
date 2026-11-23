package user_case.AddToWatchlist;

import entity.WatchlistMovie;
import entity.WatchlistMovieFactory;

public class AddToWatchlistInteractor implements AddToWatchlistInputBoundary {
    private final AddToWatchlistDataAccessInterface dataAccess;
    private final AddToWatchlistOutputBoundary presenter;
    private final WatchlistMovieFactory movieFactory;

    public AddToWatchlistInteractor(AddToWatchlistDataAccessInterface dataAccess,
                                    AddToWatchlistOutputBoundary presenter, WatchlistMovieFactory movieFactory){
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.movieFactory = movieFactory;
    }

    @Override
    public void execute(AddToWatchlistInputData inputData) {
        final String username = inputData.getUsername();
        final String movieId = inputData.getMovieId();
        final String movieTitle = inputData.getMovieTitle();
        final String posterUrl = inputData.getPosterUrl();


        // Step 2: check that the movie is not already in the watchlist (extension 2a)
        if (dataAccess.isInWatchlist(username, movieId)) {
            presenter.prepareFailView(movieTitle + " is already in your watchlist.");
            return;
        }

        // Step 3: add the movie to the watchlist and save it
        final WatchlistMovie movie = movieFactory.create(movieId, movieTitle, posterUrl);
        dataAccess.addToWatchlist(username, movie);

        // Step 4: show a confirmation message
        final AddToWatchlistOutputData outputData = new AddToWatchlistOutputData(
                movieId, movieTitle, posterUrl, movieTitle + " was added to your watchlist.");
        presenter.prepareSuccessView(outputData);
    }
}

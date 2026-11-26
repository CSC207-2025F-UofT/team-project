package user_case.ViewWatchlist;

import entity.WatchlistMovie;

import java.util.List;

public class ViewWatchlistInteractor implements ViewWatchlistInputBoundary {
    private final ViewWatchlistDataAccessInterface dataAccess;
    private final ViewWatchlistOutputBoundary presenter;

    public ViewWatchlistInteractor(ViewWatchlistDataAccessInterface dataAccess,
                                    ViewWatchlistOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewWatchlistInputData inputData) {
        final String username = inputData.getUsername();

        // Step 1: make sure we know which user's watchlist to load.
        if (username == null || username.isEmpty()) {
            presenter.prepareFailView("No user is logged in.");
            return;
        }

        // Step 2: ask the data access object for the saved watchlist.
        final List<WatchlistMovie> movies = dataAccess.getWatchlist(username);

        // Step 3: hand the movies to the presenter so the view can display them.
        final ViewWatchlistOutputData outputData = new ViewWatchlistOutputData(username, movies);
        presenter.prepareSuccessView(outputData);
    }
}

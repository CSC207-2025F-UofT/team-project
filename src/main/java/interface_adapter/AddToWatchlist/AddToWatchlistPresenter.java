package interface_adapter.AddToWatchlist;

import interface_adapter.ViewWatchlist.WatchlistState;
import interface_adapter.ViewWatchlist.WatchlistViewModel;
import user_case.AddToWatchlist.AddToWatchlistOutputBoundary;
import user_case.AddToWatchlist.AddToWatchlistOutputData;

/**
 * Presenter for the Add to Watchlist use case.
 * On success, it shows a confirmation AND adds the movie to the watchlist page's
 * view model (interface_adapter.ViewWatchlist.WatchlistViewModel), so the watchlist
 * page is up to date without having to reload it from the View Watchlist use case.
 */
public class AddToWatchlistPresenter implements AddToWatchlistOutputBoundary {
    private final AddToWatchlistViewModel addToWatchlistViewModel;
    private final WatchlistViewModel watchlistViewModel;

    public AddToWatchlistPresenter(AddToWatchlistViewModel addToWatchlistViewModel,
                                   WatchlistViewModel watchlistViewModel) {
        this.addToWatchlistViewModel = addToWatchlistViewModel;
        this.watchlistViewModel = watchlistViewModel;
    }

    @Override
    public void prepareSuccessView(AddToWatchlistOutputData outputData) {
        // 1. Update the watchlist page with the new movie
        final WatchlistState watchlistState = watchlistViewModel.getState();
        watchlistState.addItem(new WatchlistState.Item(
                outputData.getMovieId(), outputData.getMovieTitle(), outputData.getPosterUrl()));
        watchlistViewModel.setState(watchlistState);
        watchlistViewModel.fireStateChanged();

        // 2. Show the confirmation message
        final AddToWatchlistState state = addToWatchlistViewModel.getState();
        state.setSuccess(true);
        state.setMessage(outputData.getMessage());
        addToWatchlistViewModel.setState(state);
        addToWatchlistViewModel.fireStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final AddToWatchlistState state = addToWatchlistViewModel.getState();
        state.setSuccess(false);
        state.setMessage(errorMessage);
        addToWatchlistViewModel.setState(state);
        addToWatchlistViewModel.fireStateChanged();
    }
}

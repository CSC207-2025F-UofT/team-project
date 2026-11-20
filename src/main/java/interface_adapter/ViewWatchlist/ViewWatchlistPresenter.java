package interface_adapter.ViewWatchlist;

import entity.WatchlistMovie;
import user_case.ViewWatchlist.ViewWatchlistOutputBoundary;
import user_case.ViewWatchlist.ViewWatchlistOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the View Watchlist use case.
 * Turns the loaded movies into a WatchlistState the view can render.
 */
public class ViewWatchlistPresenter implements ViewWatchlistOutputBoundary {
    private final WatchlistViewModel watchlistViewModel;

    public ViewWatchlistPresenter(WatchlistViewModel watchlistViewModel) {
        this.watchlistViewModel = watchlistViewModel;
    }

    @Override
    public void prepareSuccessView(ViewWatchlistOutputData outputData) {
        final List<WatchlistState.Item> items = new ArrayList<>();
        for (WatchlistMovie movie : outputData.getMovies()) {
            items.add(new WatchlistState.Item(movie.getId(), movie.getTitle(), movie.getPosterUrl()));
        }

        final WatchlistState state = watchlistViewModel.getState();
        state.setUsername(outputData.getUsername());
        state.setItems(items);
        state.setErrorMessage("");
        watchlistViewModel.setState(state);
        watchlistViewModel.fireStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final WatchlistState state = watchlistViewModel.getState();
        state.setErrorMessage(errorMessage);
        watchlistViewModel.setState(state);
        watchlistViewModel.fireStateChanged();
    }
}

package interface_adapter.AddToWatchlist;

import user_case.AddToWatchlist.AddToWatchlistOutputBoundary;
import user_case.AddToWatchlist.AddToWatchlistOutputData;

/**
 * Presenter for the Add to Watchlist use case.
 * Updates the AddToWatchlistViewModel with a confirmation or error message.
 */
public class AddToWatchlistPresenter implements AddToWatchlistOutputBoundary {
    private final AddToWatchlistViewModel addToWatchlistViewModel;

    public AddToWatchlistPresenter(AddToWatchlistViewModel addToWatchlistViewModel) {
        this.addToWatchlistViewModel = addToWatchlistViewModel;
    }

    @Override
    public void prepareSuccessView(AddToWatchlistOutputData outputData) {
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

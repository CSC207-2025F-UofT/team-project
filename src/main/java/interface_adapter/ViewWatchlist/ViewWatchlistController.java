package interface_adapter.ViewWatchlist;

import user_case.ViewWatchlist.ViewWatchlistInputBoundary;
import user_case.ViewWatchlist.ViewWatchlistInputData;

/**
 * Controller for the View Watchlist use case.
 * Called by the view whenever the watchlist page needs to (re)load its data,
 * e.g. when the page is opened, or right after a movie was added.
 */
public class ViewWatchlistController {
    private final ViewWatchlistInputBoundary interactor;

    public ViewWatchlistController(ViewWatchlistInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads the watchlist belonging to the given user.
     * @param username the logged-in user
     */
    public void execute(String username) {
        interactor.execute(new ViewWatchlistInputData(username));
    }
}

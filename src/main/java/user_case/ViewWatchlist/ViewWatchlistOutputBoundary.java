package user_case.ViewWatchlist;

/**
 * Output boundary for the View Watchlist use case.
 */
public interface ViewWatchlistOutputBoundary {

    /**
     * Called when the watchlist was loaded successfully.
     * @param outputData the username and the movies in their watchlist
     */
    void prepareSuccessView(ViewWatchlistOutputData outputData);

    /**
     * Called when the watchlist could not be loaded (e.g. no user is logged in).
     * @param errorMessage the explanation shown to the user
     */
    void prepareFailView(String errorMessage);
}

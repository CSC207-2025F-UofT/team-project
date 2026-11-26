package user_case.ViewWatchlist;

public interface ViewWatchlistInputBoundary {
    /**
     * Execute the use case View Watchlist.
     * @param inputData the username whose watchlist should be loaded
     */
    void execute(ViewWatchlistInputData inputData);
}

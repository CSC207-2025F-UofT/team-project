package user_case.AddToWatchlist;

public interface AddToWatchlistInputBoundary {
    /**
     * Execute the use case Add to Watchlist.
     * @param inputData Movie and Username Data to add
     */
    void execute(AddToWatchlistInputData inputData);
}

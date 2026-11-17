package user_case.AddToWatchlist;

public interface AddToWatchlistInputBoundary {
    /**
     * Run the use case Add to Watchlist.
     * @param inputData Movie and Username Data
     */
    void execute(AddToWatchlistInputData inputData);
}

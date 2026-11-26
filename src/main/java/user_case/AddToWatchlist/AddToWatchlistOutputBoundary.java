package user_case.AddToWatchlist;

/**
 * Output boundary for the Add to Watchlist use case.
 */
public interface AddToWatchlistOutputBoundary {

    /**
     * Called when the movie was added to the watchlist.
     * @param outputData the added movie and a confirmation message
     */
    void prepareSuccessView(AddToWatchlistOutputData outputData);

    /**
     * Called when the movie could not be added (e.g. it is already in the watchlist).
     * @param errorMessage the explanation shown to the user
     */
    void prepareFailView(String errorMessage);
}

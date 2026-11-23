package interface_adapter.AddToWatchlist;

/**
 * Implemented by views that react to the result of Add to Watchlist.
 */
public interface AddToWatchlistViewModelListener {

    /**
     * Called whenever the presenter updates the state.
     * @param state the new state
     */
    void onAddToWatchlistStateChanged(AddToWatchlistState state);
}

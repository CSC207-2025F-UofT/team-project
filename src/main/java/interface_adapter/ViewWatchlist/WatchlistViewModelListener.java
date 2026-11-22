package interface_adapter.ViewWatchlist;

/**
 * Implemented by views that display the watchlist.
 */
public interface WatchlistViewModelListener {

    /**
     * Called whenever the watchlist changes.
     * @param state the new watchlist state
     */
    void onWatchlistStateChanged(WatchlistState state);
}

package interface_adapter.ViewWatchlist;

import java.util.ArrayList;
import java.util.List;

/**
 * View model for the watchlist page.
 */
public class WatchlistViewModel {
    public static final String VIEW_NAME = "watchlist";

    private final List<WatchlistViewModelListener> listeners = new ArrayList<>();
    private WatchlistState state = new WatchlistState();

    public String getViewName() {
        return VIEW_NAME;
    }

    public WatchlistState getState() {
        return state;
    }

    public void setState(WatchlistState state) {
        this.state = state;
    }

    /**
     * Registers a view that wants to be notified when the watchlist changes.
     * @param listener the view
     */
    public void addListener(WatchlistViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all registered views that the watchlist changed.
     */
    public void fireStateChanged() {
        for (WatchlistViewModelListener listener : listeners) {
            listener.onWatchlistStateChanged(state);
        }
    }
}

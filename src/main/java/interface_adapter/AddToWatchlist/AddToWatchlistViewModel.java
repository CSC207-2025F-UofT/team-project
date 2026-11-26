package interface_adapter.AddToWatchlist;

import java.util.ArrayList;
import java.util.List;

public class AddToWatchlistViewModel {
    private final List<AddToWatchlistViewModelListener> listeners = new ArrayList<>();
    private AddToWatchlistState state = new AddToWatchlistState();

    public AddToWatchlistState getState() {
        return state;
    }

    public void setState(AddToWatchlistState state) {
        this.state = state;
    }

    /**
     * Registers a view that wants to be notified when the state changes.
     * @param listener the view
     */
    public void addListener(AddToWatchlistViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all registered views that the state changed.
     */
    public void fireStateChanged() {
        for (AddToWatchlistViewModelListener listener : listeners) {
            listener.onAddToWatchlistStateChanged(state);
        }
    }
}

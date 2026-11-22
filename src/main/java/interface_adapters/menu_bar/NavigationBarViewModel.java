package interface_adapters.menu_bar;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Navigation Bar.
 */

public class NavigationBarViewModel {

    private final List<NavigationBarViewModelListener> listeners = new ArrayList<NavigationBarViewModelListener>();
    private NavigationBarState state = new NavigationBarState();

    public NavigationBarState getState() {
        return state;
    }

    public void setState(NavigationBarState state) {
        this.state = state;
    }

    /**
     * Registers a listener to be notified when the Navigation Bar View Model changes.
     * @param listener a View
     */
    public void addListener(NavigationBarViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the view state has changed.
     */
    public void notifyStateChanged() {
        for (NavigationBarViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

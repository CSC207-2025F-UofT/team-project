package interface_adapters.home;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Home View.
 */
public class HomeViewModel {
    private HomeState state = new HomeState();
    private List<HomeViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "home";

    /**
     * Returns the current state of the home screen.
     * @return the current HomeState object
     */
    public HomeState getState() {
        return state;
    }

    /**
     * Sets the home screen's state.
     * @param state the new state to store
     */
    public void setState(HomeState state) {
        this.state = state;
    }

    /**
     * Registers a listener to be notified when the Home View Model changes.
     * @param listener a View
     */
    public void addListener(HomeViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the view state has changed.
     */
    public void notifyStateChanged() {
        for (HomeViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}

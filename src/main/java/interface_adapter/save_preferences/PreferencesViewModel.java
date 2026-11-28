package interface_adapter.save_preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * The view model of the My profile page, written the same way as the other view models in the project.
 */
public class PreferencesViewModel {
    /** Name the page is registered under in the view manager. */
    public static final String VIEW_NAME = "my profile";

    private final List<PreferencesViewModelListener> listeners = new ArrayList<>();
    private PreferencesState state = new PreferencesState();

    /**
     * Returns the current state.
     *
     * @return the state
     */
    public PreferencesState getState() {
        return state;
    }

    /**
     * Replaces the state. Call {@link #notifyStateChanged()} afterwards to tell the view.
     *
     * @param state the new state
     */
    public void setState(PreferencesState state) {
        this.state = state;
    }

    /**
     * Registers a listener.
     *
     * @param listener the listener to notify
     */
    public void addListener(PreferencesViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener the state changed.
     */
    public void notifyStateChanged() {
        for (PreferencesViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

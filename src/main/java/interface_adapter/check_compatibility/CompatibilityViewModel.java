package interface_adapter.check_compatibility;

import java.util.ArrayList;
import java.util.List;

/**
 * The view model of the compatibility panel, written the same way as the other view models in the project.
 */
public class CompatibilityViewModel {
    private final List<CompatibilityViewModelListener> listeners = new ArrayList<>();
    private CompatibilityState state = new CompatibilityState();

    /**
     * Returns the current state.
     *
     * @return the state
     */
    public CompatibilityState getState() {
        return state;
    }

    /**
     * Replaces the state. Call {@link #notifyStateChanged()} afterwards to tell the view.
     *
     * @param state the new state
     */
    public void setState(CompatibilityState state) {
        this.state = state;
    }

    /**
     * Registers a listener.
     *
     * @param listener the listener to notify
     */
    public void addListener(CompatibilityViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener the state changed.
     */
    public void notifyStateChanged() {
        for (CompatibilityViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

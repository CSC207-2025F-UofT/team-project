package interface_adapter.find_nearby_quests;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Holds the map's state and tells listening views when it changes (Observer pattern).
 * It does not know which view is listening.
 */
public class FindNearbyQuestsViewModel {
    /** Name of the property fired when the state changes. */
    public static final String STATE_PROPERTY = "state";
    /** Name the view manager can use for this view. */
    public static final String VIEW_NAME = "map";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private FindNearbyQuestsState state;

    /**
     * Creates the view model.
     *
     * @param initialRadiusKm the radius to show before the first search
     */
    public FindNearbyQuestsViewModel(double initialRadiusKm) {
        state = new FindNearbyQuestsState();
        state.setRadiusKm(initialRadiusKm);
    }

    /**
     * Returns the current state.
     *
     * @return the state
     */
    public FindNearbyQuestsState getState() {
        return state;
    }

    /**
     * Replaces the current state. Call {@link #firePropertyChanged()} afterwards.
     *
     * @param state the new state
     */
    public void setState(FindNearbyQuestsState state) {
        this.state = state;
    }

    /** Tells every listener that the state changed. */
    public void firePropertyChanged() {
        support.firePropertyChange(STATE_PROPERTY, null, state);
    }

    /**
     * Registers a listener for state changes.
     *
     * @param listener the listener, usually the map view
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}

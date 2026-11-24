package interface_adapter.explore;

import java.util.ArrayList;
import java.util.List;

/**
 * View model for the Explore screen. Holds an ExploreState that the presenters
 * update and the ExploreView displays.
 *
 * <p>Follows the same pattern as the team's other view models (e.g. LoginViewModel):
 * listeners register with addListener(), and presenters call notifyStateChanged()
 * after changing the state. This is the Observer design pattern.</p>
 */
public class ExploreViewModel {
    /** Name of this screen in the CardLayout. */
    public static final String VIEW_NAME = "explore";

    /** Everyone who wants to hear about changes (normally just the ExploreView). */
    private final List<ExploreViewModelListener> listeners = new ArrayList<>();

    /** The data the screen displays. */
    private ExploreState state = new ExploreState();

    /** @return the current state */
    public ExploreState getState() {
        return this.state;
    }

    /**
     * Replaces the state. Listeners are not told until notifyStateChanged() is called.
     *
     * @param state the new state
     */
    public void setState(ExploreState state) {
        this.state = state;
    }

    /** @return this screen's name in the CardLayout */
    public String getViewName() {
        return VIEW_NAME;
    }

    /**
     * Registers a listener (the ExploreView).
     *
     * @param listener the listener
     */
    public void addListener(ExploreViewModelListener listener) {
        listeners.add(listener);
    }

    /** Tells all listeners that the state changed, so the screen redraws. */
    public void notifyStateChanged() {
        for (ExploreViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

    /**
     * Tells all listeners that the inventory is full. This is separate from
     * notifyStateChanged() so the view knows to open the "drop which item?" dialog
     * instead of just redrawing.
     */
    public void notifyInventoryFull() {
        for (ExploreViewModelListener listener : listeners) {
            listener.inventoryFull();
        }
    }
}

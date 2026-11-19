package interface_adapter.shopping_list;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the shopping list page.
 */
public class ShoppingListViewModel {

    public static final String VIEW_NAME = "shopping list";

    private ShoppingListState state = new ShoppingListState();
    private final List<ShoppingListViewModelListener> listeners = new ArrayList<>();

    /**
     * Listener notified when the shopping list state changes.
     */
    public interface ShoppingListViewModelListener {

        /**
         * Called after the view model state has been updated.
         */
        void stateChanged();
    }

    /**
     * Returns the current state.
     *
     * @return the state
     */
    public ShoppingListState getState() {
        return state;
    }

    /**
     * Replaces the state without notifying listeners.
     *
     * @param state the new state
     */
    public void setState(ShoppingListState state) {
        this.state = state;
    }

    /**
     * Registers a listener for state changes.
     *
     * @param listener the listener
     */
    public void addListener(ShoppingListViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the state has changed.
     */
    public void notifyStateChanged() {
        for (ShoppingListViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

package interface_adapter.add_expense;

import java.util.ArrayList;
import java.util.List;

/**
 * View model for {@link view.AddExpenseView}. Follows the same custom listener pattern as {@link
 * interface_adapter.login.LoginViewModel}.
 */
public class AddExpenseViewModel {

    /** CardLayout identifier used by {@link view.ViewManager} for this screen. */
    public static final String VIEW_NAME = "add expense";

    private final List<AddExpenseViewModelListener> listeners = new ArrayList<>();
    private AddExpenseState state = new AddExpenseState();

    /**
     * Returns the current {@link AddExpenseState}.
     *
     * @return the current state
     */
    public AddExpenseState getState() {
        return state;
    }

    /**
     * Replaces the current {@link AddExpenseState}.
     *
     * @param state the new state
     */
    public void setState(AddExpenseState state) {
        this.state = state;
    }

    /**
     * Registers a listener to be notified when the state changes.
     *
     * @param listener the {@link AddExpenseViewModelListener} to register
     */
    public void addListener(AddExpenseViewModelListener listener) {
        listeners.add(listener);
    }

    /** Notifies all registered listeners that the state has changed. */
    public void notifyStateChanged() {
        for (AddExpenseViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
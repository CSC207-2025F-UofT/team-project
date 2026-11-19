package interface_adapter.meal_plan;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the meal plan page (use case 3).
 */
public class MealPlanViewModel {

    public static final String VIEW_NAME = "meal plan";

    private MealPlanState state = new MealPlanState();
    private final List<MealPlanViewModelListener> listeners = new ArrayList<>();

    /**
     * Listener notified when the meal plan state changes.
     */
    public interface MealPlanViewModelListener {

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
    public MealPlanState getState() {
        return state;
    }

    /**
     * Replaces the state without notifying listeners.
     *
     * @param state the new state
     */
    public void setState(MealPlanState state) {
        this.state = state;
    }

    /**
     * Registers a listener for state changes.
     *
     * @param listener the listener
     */
    public void addListener(MealPlanViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the state has changed.
     */
    public void notifyStateChanged() {
        for (MealPlanViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

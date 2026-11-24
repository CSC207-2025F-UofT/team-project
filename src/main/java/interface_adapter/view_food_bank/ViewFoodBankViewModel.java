package interface_adapter.view_food_bank;

import entity.FoodItem;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * View model for the View Food Bank use case.
 *
 * Stores the list of food items to be displayed by the
 * Food Bank view, along with the success status and
 * user-facing message of the most recent retrieval.
 *
 * Notifies registered listeners whenever the food bank
 * result is updated by the presenter.
 *
 * The UI state is stored directly in this class rather
 * than in a separate state object.
 */
public class ViewFoodBankViewModel {

    /**
     * Property name used when notifying listeners that
     * the food bank result has changed.
     */
    public static final String FOOD_BANK_PROPERTY = "foodBankResult";

    // Foods currently available for display.
    private List<FoodItem> foods = new ArrayList<>();

    // Status of the most recent retrieval operation.
    private boolean success;

    // User-facing success or error message.
    private String message = "";

    // Manages change notifications for the Swing view.
    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    /**
     * Returns the food items currently stored in the view model.
     *
     * The returned list cannot be modified by the caller.
     *
     * @return unmodifiable list of food items
     */
    public List<FoodItem> getFoods() {
        return Collections.unmodifiableList(foods);
    }

    /**
     * Returns whether the most recent retrieval succeeded.
     *
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the message intended for the user.
     *
     * @return success or error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the view model after successfully retrieving
     * the food bank.
     *
     * Creates a defensive copy of the supplied list to avoid
     * exposing externally mutable collections.
     *
     * Notifies listeners after all fields have been updated.
     *
     * @param foods list of retrieved foods
     * @param message message to display to the user
     * @throws IllegalArgumentException if foods is null
     */
    public void setSuccessResult(
            List<FoodItem> foods, String message) {

        if (foods == null) {
            throw new IllegalArgumentException(
                    "Food list cannot be null.");
        }

        this.foods = new ArrayList<>(foods);
        this.success = true;
        this.message = message;

        // Notify the view after updating the complete result.
        firePropertyChanged();
    }

    /**
     * Updates the view model after a failed food bank retrieval.
     *
     * Clears previously displayed food information to avoid
     * showing stale results following a failure.
     *
     * @param errorMessage description of the failure
     */
    public void setFailureResult(String errorMessage) {

        this.foods = new ArrayList<>();
        this.success = false;
        this.message = errorMessage;

        // Notify the view that the retrieval failed.
        firePropertyChanged();
    }

    /**
     * Registers a listener to observe view model changes.
     *
     * @param listener listener to register
     */
    public void addPropertyChangeListener(
            PropertyChangeListener listener) {

        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a previously registered listener.
     *
     * @param listener listener to remove
     */
    public void removePropertyChangeListener(
            PropertyChangeListener listener) {

        support.removePropertyChangeListener(listener);
    }

    /**
     * Notifies listeners that the food bank result has changed.
     */
    private void firePropertyChanged() {
        support.firePropertyChange(
                FOOD_BANK_PROPERTY, null, this);
    }
}
package interface_adapter.add_food;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View model for the Add Food use case.
 *
 * Stores the information displayed by the Add Food view,
 * including the newly created food, nutritional values,
 * operation status, and user-facing message.
 *
 * Notifies registered listeners whenever the view model
 * is updated by the presenter.
 */
public class AddFoodViewModel {

    // Identifier of the newly created food.
    private String foodId;

    // Name of the newly created food.
    private String foodName;

    // Nutritional values per 100 grams.
    private double calories;
    private double protein;
    private double carbs;
    private double fat;

    // Status and message of the most recent operation.
    private boolean success;
    private String message = "";

    // Manages listeners that observe changes to this view model.
    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    /**
     * Returns the ID of the newly created food.
     *
     * @return food ID, or null if no food was created
     */
    public String getFoodId() {
        return foodId;
    }

    /**
     * Returns the name of the newly created food.
     *
     * @return food name, or null if no food was created
     */
    public String getFoodName() {
        return foodName;
    }

    /**
     * Returns the calories per 100 grams.
     *
     * @return calories
     */
    public double getCalories() {
        return calories;
    }

    /**
     * Returns the protein per 100 grams.
     *
     * @return protein in grams
     */
    public double getProtein() {
        return protein;
    }

    /**
     * Returns the carbohydrates per 100 grams.
     *
     * @return carbohydrates in grams
     */
    public double getCarbs() {
        return carbs;
    }

    /**
     * Returns the fat per 100 grams.
     *
     * @return fat in grams
     */
    public double getFat() {
        return fat;
    }

    /**
     * Returns whether the most recent operation succeeded.
     *
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the message for the user.
     *
     * @return success or failure message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the view model after a successful Add Food operation.
     *
     * @param foodId ID of the created food
     * @param foodName name of the created food
     * @param calories calories per 100 grams
     * @param protein protein per 100 grams
     * @param carbs carbohydrates per 100 grams
     * @param fat fat per 100 grams
     * @param message user-facing success message
     */
    public void setSuccessResult(
            String foodId, String foodName,
            double calories, double protein,
            double carbs, double fat, String message) {

        this.foodId = foodId;
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.success = true;
        this.message = message;

        // Notify the view after all fields have been updated.
        firePropertyChanged();
    }

    /**
     * Updates the view model after a failed Add Food operation.
     *
     * Clears previously created food information to avoid
     * displaying stale results.
     *
     * @param errorMessage description of the failure
     */
    public void setFailureResult(String errorMessage) {

        this.foodId = null;
        this.foodName = null;
        this.calories = 0;
        this.protein = 0;
        this.carbs = 0;
        this.fat = 0;
        this.success = false;
        this.message = errorMessage;

        // Notify the view about the failed operation.
        firePropertyChanged();
    }

    /**
     * Registers a listener for changes to the view model.
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
     * Notifies listeners that the view model has changed.
     */
    private void firePropertyChanged() {
        support.firePropertyChange("addFoodResult", null, this);
    }
}

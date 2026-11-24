package interface_adapter.view_food_bank;

import entity.FoodItem;
import use_case.view_food_bank.ViewFoodBankOutputBoundary;
import use_case.view_food_bank.ViewFoodBankOutputData;

import java.util.List;

/**
 * Presenter for the View Food Bank use case.
 *
 * Converts the results provided by the use case into
 * information suitable for the user interface.
 *
 * Updates the ViewFoodBankViewModel, which notifies
 * registered listeners whenever its state changes.
 *
 * This class does not depend directly on Swing components
 * or data access implementations.
 */
public class ViewFoodBankPresenter
        implements ViewFoodBankOutputBoundary {

    // View model updated by this presenter.
    private final ViewFoodBankViewModel viewModel;

    /**
     * Constructs a View Food Bank presenter.
     *
     * @param viewModel view model to update
     * @throws IllegalArgumentException if viewModel is null
     */
    public ViewFoodBankPresenter(ViewFoodBankViewModel viewModel) {

        if (viewModel == null) {
            throw new IllegalArgumentException(
                    "View Food Bank view model cannot be null.");
        }

        this.viewModel = viewModel;
    }

    /**
     * Prepares the view model after successfully retrieving
     * the food bank.
     *
     * Updates the list of foods and generates a message
     * describing how many food items were retrieved.
     *
     * An empty food bank is considered a successful result.
     *
     * @param outputData output data containing retrieved foods
     */
    @Override
    public void prepareSuccessView(ViewFoodBankOutputData outputData) {

        List<FoodItem> foods = outputData.getFoods();

        String message;

        if (foods.isEmpty()) {
            message = "No foods have been added yet.";
        } else if (foods.size() == 1) {
            message = "1 food found.";
        } else {
            message = foods.size() + " foods found.";
        }

        // Update the view model and notify its listeners.
        viewModel.setSuccessResult(foods, message);
    }

    /**
     * Prepares the view model after a failed food bank retrieval.
     *
     * Clears previously displayed results and forwards the
     * error message for presentation by the view.
     *
     * @param errorMessage explanation of the retrieval failure
     */
    @Override
    public void prepareFailView(String errorMessage) {

        viewModel.setFailureResult(errorMessage);
    }
}
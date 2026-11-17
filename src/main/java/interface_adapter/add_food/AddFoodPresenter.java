package interface_adapter.add_food;

import use_case.add_food.AddFoodOutputBoundary;
import use_case.add_food.AddFoodOutputData;

/**
 * Presenter for the Add Food use case.
 *
 * Converts use-case output into information suitable for
 * the user interface and updates the AddFoodViewModel.
 *
 * Does not depend directly on Swing components.
 */
public class AddFoodPresenter implements AddFoodOutputBoundary {

    // View model updated by this presenter.
    private final AddFoodViewModel viewModel;

    /**
     * Constructs an Add Food presenter.
     *
     * @param viewModel view model to update
     * @throws IllegalArgumentException if viewModel is null
     */
    public AddFoodPresenter(AddFoodViewModel viewModel) {

        if (viewModel == null) {
            throw new IllegalArgumentException(
                    "Add Food view model cannot be null.");
        }

        this.viewModel = viewModel;
    }

    /**
     * Prepares the view model after successfully adding a food.
     *
     * @param outputData information about the created food
     */
    @Override
    public void prepareSuccessView(AddFoodOutputData outputData) {

        String message =
                outputData.getFoodName() + " added successfully.";

        viewModel.setSuccessResult(
                outputData.getFoodId(),
                outputData.getFoodName(),
                outputData.getCalories(),
                outputData.getProtein(),
                outputData.getCarbs(),
                outputData.getFat(),
                message
        );
    }

    /**
     * Prepares the view model after a failed Add Food operation.
     *
     * @param errorMessage explanation of the failure
     */
    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setFailureResult(errorMessage);
    }
}

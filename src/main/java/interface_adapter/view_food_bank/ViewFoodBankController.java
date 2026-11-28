package interface_adapter.view_food_bank;

import use_case.view_food_bank.ViewFoodBankInputBoundary;

/**
 * Controller for the View Food Bank use case.
 *
 * Receives requests from the user interface and delegates
 * execution to the View Food Bank input boundary.
 *
 * This class does not contain business logic or depend
 * directly on a concrete interactor implementation.
 */
public class ViewFoodBankController {

    // Input boundary responsible for executing the use case.
    private final ViewFoodBankInputBoundary viewFoodBankUseCase;

    /**
     * Constructs a View Food Bank controller.
     *
     * @param viewFoodBankUseCase input boundary for the use case
     * @throws IllegalArgumentException if the input boundary is null
     */
    public ViewFoodBankController(
            ViewFoodBankInputBoundary viewFoodBankUseCase) {

        if (viewFoodBankUseCase == null) {
            throw new IllegalArgumentException(
                    "View Food Bank input boundary cannot be null.");
        }

        this.viewFoodBankUseCase = viewFoodBankUseCase;
    }

    /**
     * Executes the View Food Bank use case.
     *
     * Requests that all stored foods be retrieved and
     * presented through the output boundary.
     */
    public void execute() {
        viewFoodBankUseCase.execute();
    }
}

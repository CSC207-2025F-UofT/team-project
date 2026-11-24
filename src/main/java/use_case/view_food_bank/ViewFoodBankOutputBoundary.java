package use_case.view_food_bank;

/**
 * Output boundary for the View Food Bank use case.
 *
 * Defines how the interactor communicates the result of
 * retrieving food items to the presentation layer.
 *
 * The ViewFoodBankPresenter implements this interface,
 * allowing the interactor to remain independent of
 * user interface implementation details.
 */
public interface ViewFoodBankOutputBoundary {

    /**
     * Prepares the successful result of retrieving the food bank.
     *
     * The output data contains the food items retrieved
     * from the repository.
     *
     * An empty food bank is considered a successful result,
     * with an empty list of foods.
     *
     * @param outputData result of the View Food Bank use case
     */
    void prepareSuccessView(ViewFoodBankOutputData outputData);

    /**
     * Prepares an error message when the food bank
     * cannot be retrieved.
     *
     * @param errorMessage description of the failure
     */
    void prepareFailView(String errorMessage);
}

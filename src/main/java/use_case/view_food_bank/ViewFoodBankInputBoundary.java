
package use_case.view_food_bank;

/**
 * Input boundary for the View Food Bank use case.
 *
 * Defines the operation available to the controller for
 * retrieving and displaying foods stored in the food bank.
 *
 * The ViewFoodBankInteractor implements this interface,
 * allowing the controller to depend on an abstraction
 * rather than a concrete use case implementation.
 */
public interface ViewFoodBankInputBoundary {

    /**
     * Executes the View Food Bank use case.
     *
     * Retrieves all stored foods and passes the results
     * to the output boundary for presentation.
     */
    void execute();
}

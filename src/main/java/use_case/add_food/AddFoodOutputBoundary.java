package use_case.add_food;

/**
 * Defines the output boundary for the Add Food use case.
 *
 * The Add Food interactor uses this interface to communicate
 * the result of its execution without depending on a specific
 * presenter or user interface.
 *
 * The presenter implements this interface and converts the
 * result into a format suitable for the view.
 */
public interface AddFoodOutputBoundary {

    /**
     * Handles the successful creation of a food item.
     *
     * @param outputData information about the newly created food
     */
    void prepareSuccessView(AddFoodOutputData outputData);

    /**
     * Handles a failure to create a food item.
     *
     * @param errorMessage description of why the operation failed
     */
    void prepareFailView(String errorMessage);
}

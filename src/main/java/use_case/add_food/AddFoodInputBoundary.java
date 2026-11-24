package use_case.add_food;

/**
 * Defines the input boundary for the Add Food use case.
 */
public interface AddFoodInputBoundary {

    /**
     * Executes the Add Food use case.
     *
     * @param inputData information required to add a new food
     */
    void execute(AddFoodInputData inputData);
}
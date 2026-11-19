package use_case.generate_shopping_list;

/**
 * Output boundary for generating a shopping list.
 */
public interface GenerateShoppingListOutputBoundary {

    /**
     * Presents a successful generation.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(GenerateShoppingListOutputData outputData);

    /**
     * Presents a failed generation.
     *
     * @param outputData the output data
     */
    void prepareFailView(GenerateShoppingListOutputData outputData);
}

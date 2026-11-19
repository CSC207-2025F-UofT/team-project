package use_case.generate_shopping_list;

/**
 * Input boundary for generating a shopping list.
 */
public interface GenerateShoppingListInputBoundary {

    /**
     * Generates a shopping list for the current week.
     *
     * @param inputData the input data
     */
    void execute(GenerateShoppingListInputData inputData);
}

package use_case.load_shopping_list;

/**
 * Input boundary for loading a shopping list.
 */
public interface LoadShoppingListInputBoundary {

    /**
     * Loads the shopping list for the requested week.
     *
     * @param inputData the input data
     */
    void execute(LoadShoppingListInputData inputData);
}

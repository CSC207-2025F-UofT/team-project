package use_case.load_shopping_list;

/**
 * Output boundary for loading a shopping list.
 */
public interface LoadShoppingListOutputBoundary {

    /**
     * Presents a successfully loaded list.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(LoadShoppingListOutputData outputData);
}

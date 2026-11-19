package use_case.generate_shopping_list;

import entity.ShoppingList;

/**
 * Output data for generating a shopping list.
 */
public class GenerateShoppingListOutputData {

    private final ShoppingList shoppingList;
    private final String message;
    private final boolean useCaseFailed;

    /**
     * Creates output data.
     *
     * @param shoppingList the generated list, or null on failure
     * @param message an info or error message
     * @param useCaseFailed true if generation failed
     */
    public GenerateShoppingListOutputData(ShoppingList shoppingList,
                                          String message,
                                          boolean useCaseFailed) {
        this.shoppingList = shoppingList;
        this.message = message;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * Returns the shopping list.
     *
     * @return the shopping list
     */
    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    /**
     * Returns the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns whether the use case failed.
     *
     * @return true if failed
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}

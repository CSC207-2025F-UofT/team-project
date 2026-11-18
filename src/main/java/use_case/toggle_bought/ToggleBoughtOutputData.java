package use_case.toggle_bought;

import entity.ShoppingList;

/**
 * Output data for toggling bought.
 */
public class ToggleBoughtOutputData {

    private final ShoppingList shoppingList;
    private final String message;
    private final boolean useCaseFailed;

    /**
     * Creates output data.
     *
     * @param shoppingList the updated list, or null on failure
     * @param message an error message when failed
     * @param useCaseFailed true if the toggle failed
     */
    public ToggleBoughtOutputData(ShoppingList shoppingList,
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

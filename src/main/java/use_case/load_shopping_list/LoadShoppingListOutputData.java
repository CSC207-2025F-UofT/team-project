package use_case.load_shopping_list;

import entity.ShoppingList;

/**
 * Output data for loading a shopping list.
 */
public class LoadShoppingListOutputData {

    private final ShoppingList shoppingList;
    private final boolean empty;

    /**
     * Creates output data.
     *
     * @param shoppingList the list to show (never null)
     * @param empty true if there was no saved list
     */
    public LoadShoppingListOutputData(ShoppingList shoppingList, boolean empty) {
        this.shoppingList = shoppingList;
        this.empty = empty;
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
     * Returns whether the list was empty because nothing was saved.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return empty;
    }
}

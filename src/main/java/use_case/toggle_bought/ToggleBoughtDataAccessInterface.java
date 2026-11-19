package use_case.toggle_bought;

import entity.ShoppingList;

import java.time.LocalDate;

/**
 * Loads and saves a shopping list when toggling bought.
 */
public interface ToggleBoughtDataAccessInterface {

    /**
     * Returns the shopping list for the given week, or null if none is saved.
     *
     * @param weekStart the Monday that starts the week
     * @return the saved list, or null
     */
    ShoppingList loadShoppingList(LocalDate weekStart);

    /**
     * Saves a shopping list.
     *
     * @param shoppingList the list to save
     */
    void saveShoppingList(ShoppingList shoppingList);
}

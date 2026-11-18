package use_case.generate_shopping_list;

import entity.ShoppingList;

import java.time.LocalDate;

/**
 * Loads and saves shopping lists for generate.
 */
public interface GenerateShoppingListDataAccessInterface {

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

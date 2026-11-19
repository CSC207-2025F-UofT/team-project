package use_case.load_shopping_list;

import entity.ShoppingList;

import java.time.LocalDate;

/**
 * Loads a saved shopping list for a week.
 */
public interface LoadShoppingListDataAccessInterface {

    /**
     * Returns the shopping list for the given week, or null if none is saved.
     *
     * @param weekStart the Monday that starts the week
     * @return the saved list, or null
     */
    ShoppingList loadShoppingList(LocalDate weekStart);
}

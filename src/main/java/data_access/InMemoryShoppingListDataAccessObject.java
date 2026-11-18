package data_access;

import entity.ShoppingList;
import use_case.generate_shopping_list.GenerateShoppingListDataAccessInterface;
import use_case.load_shopping_list.LoadShoppingListDataAccessInterface;
import use_case.toggle_bought.ToggleBoughtDataAccessInterface;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory shopping-list store for tests and early demos.
 */
public class InMemoryShoppingListDataAccessObject
        implements LoadShoppingListDataAccessInterface,
        GenerateShoppingListDataAccessInterface,
        ToggleBoughtDataAccessInterface {

    private final Map<LocalDate, ShoppingList> lists = new HashMap<>();

    @Override
    public ShoppingList loadShoppingList(LocalDate weekStart) {
        return lists.get(weekStart);
    }

    @Override
    public void saveShoppingList(ShoppingList shoppingList) {
        lists.put(shoppingList.getWeekStart(), shoppingList);
    }

    /**
     * Seeds a list for tests or demos.
     *
     * @param shoppingList the list to store
     */
    public void seed(ShoppingList shoppingList) {
        saveShoppingList(shoppingList);
    }
}

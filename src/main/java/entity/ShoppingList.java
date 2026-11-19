package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A shopping list for one week, starting on Monday.
 */
public class ShoppingList {

    private final LocalDate weekStart;
    private final List<ShoppingItem> items;

    /**
     * Creates a shopping list for the given week.
     *
     * @param weekStart the Monday that starts the week
     * @param items the items on the list
     */
    public ShoppingList(LocalDate weekStart, List<ShoppingItem> items) {
        if (weekStart == null) {
            throw new IllegalArgumentException("Week start cannot be null");
        }
        if (items == null) {
            throw new IllegalArgumentException("Items cannot be null");
        }
        this.weekStart = weekStart;
        this.items = new ArrayList<>(items);
    }

    /**
     * Returns the Monday that starts this week.
     *
     * @return the week start
     */
    public LocalDate getWeekStart() {
        return weekStart;
    }

    /**
     * Returns an unmodifiable view of the items.
     *
     * @return the items
     */
    public List<ShoppingItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Finds an item by ingredient name.
     *
     * @param name the ingredient name
     * @return the item, or null if it is not on the list
     */
    public ShoppingItem find(IngredientName name) {
        for (ShoppingItem item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Toggles the bought flag for the item with the given name.
     *
     * @param name the ingredient name
     * @throws IllegalArgumentException if the item is not on the list
     */
    public void toggleBought(IngredientName name) {
        ShoppingItem item = find(name);
        if (item == null) {
            throw new IllegalArgumentException("Item not on shopping list");
        }
        item.setBought(!item.isBought());
    }
}

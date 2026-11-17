package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The collection of ingredients currently stored in the user's pantry.
 */
public class Pantry {
    private final Map<IngredientName, PantryItem> items = new HashMap<>();

    /**
     * Adds an item to the pantry.
     *
     * @param item the item to add
     * @throws IllegalArgumentException when the ingredient is already present
     */
    public void add(PantryItem item) {
        Objects.requireNonNull(item, "item cannot be null");
        if (items.containsKey(item.getName())) {
            throw new IllegalArgumentException("ingredient is already in the pantry");
        }
        items.put(item.getName(), item);
    }

    /**
     * Finds an item by ingredient name.
     *
     * @param name the ingredient to find
     * @return the matching item, or an empty optional when it is absent
     */
    public Optional<PantryItem> find(IngredientName name) {
        Objects.requireNonNull(name, "name cannot be null");
        return Optional.ofNullable(items.get(name));
    }

    /**
     * Removes an ingredient from the pantry.
     *
     * @param name the ingredient to remove
     * @throws IllegalArgumentException when the ingredient is absent
     */
    public void remove(IngredientName name) {
        Objects.requireNonNull(name, "name cannot be null");
        if (items.remove(name) == null) {
            throw new IllegalArgumentException("ingredient is not in the pantry");
        }
    }

    /**
     * Changes the stock level of a pantry item.
     *
     * @param name the ingredient to update
     * @param level the new stock level
     * @throws IllegalArgumentException when the ingredient is absent
     */
    public void setStockLevel(IngredientName name, StockLevel level) {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(level, "stock level cannot be null");

        final PantryItem item = items.get(name);
        if (item == null) {
            throw new IllegalArgumentException("ingredient is not in the pantry");
        }
        item.setStockLevel(level);
    }

    /**
     * Returns a snapshot of the pantry ordered by expiry date. Items without an
     * expiry date appear last.
     *
     * @return the sorted pantry items
     */
    public List<PantryItem> sortedByExpiry() {
        final List<PantryItem> sortedItems = new ArrayList<>(items.values());
        sortedItems.sort(Comparator.comparing(
                PantryItem::getExpiryDate,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));
        return sortedItems;
    }
}

package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The items a character is carrying, up to a maximum capacity.
 *
 * <p>The capacity limit is what makes extension 7a of Use Case 2
 * ("the inventory is full") possible.</p>
 */
public class Inventory {
    /** The carried items, in the order they were picked up. */
    private final List<Item> items = new ArrayList<>();
    /** The most items that can be carried at once. */
    private final int maxCapacity;

    /**
     * Creates an empty inventory.
     *
     * @param maxCapacity how many items it can hold; must be at least 1
     * @throws IllegalArgumentException if maxCapacity is 0 or negative
     */
    public Inventory(int maxCapacity) {
        // An inventory that can hold nothing makes no sense, so reject it early.
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.maxCapacity = maxCapacity;
    }

    /**
     * Adds an item if there is room.
     *
     * @param item the item to add
     * @return true if the item was added, false if the inventory is full
     */
    public boolean addItem(Item item) {
        // Refuse instead of throwing, so callers can simply check the result.
        if (isFull()) {
            return false;
        }
        items.add(item);
        return true;
    }

    /**
     * Removes the given item.
     *
     * @param item the item to remove
     * @return true if it was in the inventory
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Finds the first item with the given name.
     *
     * @param name item name
     * @return the item, or null if not carried
     */
    public Item findItem(String name) {
        // Linear search is fine: an inventory only holds a handful of items.
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    /** @return true if no more items can be added */
    public boolean isFull() {
        return items.size() >= maxCapacity;
    }

    /**
     * Returns the carried items. The list is read-only, so other classes must use
     * addItem/removeItem (which enforce the capacity) to change the inventory.
     *
     * @return an unmodifiable view of the items
     */
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    /** @return the maximum number of items */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /** @return how many items are currently carried */
    public int size() {
        return items.size();
    }
}

package use_case.explore;

import entity.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Plain data about an item that is passed out of the use case layer.
 *
 * <p>Why not just pass the Item entity? Clean Architecture says outer layers
 * (presenters, views) shouldn't depend on entities. Copying the fields into this
 * simple data object means a change to Item can't break the UI.</p>
 */
public class ItemData {
    private final String name;
    private final String description;
    /** The item type as text, e.g. "WEAPON", so outer layers don't need the ItemType enum. */
    private final String type;
    private final int value;

    /**
     * Creates the data object.
     *
     * @param name        the item's name
     * @param description the item's description
     * @param type        the item's type as text
     * @param value       the item's power
     */
    public ItemData(String name, String description, String type, int value) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    /**
     * Copies one Item entity into an ItemData.
     *
     * @param item the entity
     * @return the matching data object
     */
    public static ItemData from(Item item) {
        return new ItemData(item.getName(), item.getDescription(),
                item.getType().name(), item.getValue());
    }

    /**
     * Copies a list of Item entities, keeping their order.
     *
     * @param items the entities
     * @return the matching data objects
     */
    public static List<ItemData> fromAll(List<Item> items) {
        List<ItemData> result = new ArrayList<>();
        for (Item item : items) {
            result.add(from(item));
        }
        return result;
    }

    /** @return the item's name */
    public String getName() {
        return name;
    }

    /** @return the item's description */
    public String getDescription() {
        return description;
    }

    /** @return the item's type, e.g. "POTION" */
    public String getType() {
        return type;
    }

    /** @return the item's power */
    public int getValue() {
        return value;
    }
}

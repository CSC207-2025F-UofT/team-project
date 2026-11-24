package entity;

/**
 * A magical item lying in a room or carried in an inventory.
 *
 * <p>The value is the item's power: for example the damage bonus of a weapon,
 * the armor class bonus of armor, or the hit points restored by a potion.</p>
 *
 * <p>Items are immutable (all fields are final): once created, an item never
 * changes. Only its location (room or inventory) changes.</p>
 */
public class Item {
    /** Name shown to the player, e.g. "Potion of Healing". */
    private final String name;
    /** Longer text shown when the player examines the item (Use Case 2, step 6). */
    private final String description;
    /** Weapon, armor or potion. */
    private final ItemType type;
    /** How powerful the item is. */
    private final int value;

    /**
     * Creates an item.
     *
     * @param name        the item's name
     * @param description what the item does
     * @param type        weapon, armor or potion
     * @param value       the item's power
     */
    public Item(String name, String description, ItemType type, int value) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    /** @return the item's name */
    public String getName() {
        return name;
    }

    /** @return the item's description */
    public String getDescription() {
        return description;
    }

    /** @return weapon, armor or potion */
    public ItemType getType() {
        return type;
    }

    /** @return the item's power */
    public int getValue() {
        return value;
    }

    /**
     * Readable form for debugging, e.g. "Longsword (WEAPON, 8)".
     *
     * @return a short summary of the item
     */
    @Override
    public String toString() {
        return name + " (" + type + ", " + value + ")";
    }
}

package use_case.explore.pick_up_item;

/**
 * Input for picking up an item from the current room.
 *
 * <p>There are two constructors for the two situations in the use case: a normal
 * pick-up, and a pick-up where the player drops something to make room.</p>
 */
public class PickUpItemInputData {
    /** Name of the item in the room the player wants. */
    private final String itemName;
    /** Name of a carried item to drop in exchange, or null for a normal pick-up. */
    private final String itemToDiscard;

    /**
     * Normal pick-up (step 7).
     *
     * @param itemName name of the item in the room to pick up
     */
    public PickUpItemInputData(String itemName) {
        // Reuse the other constructor, with no item to discard.
        this(itemName, null);
    }

    /**
     * Pick-up when the inventory is full (extension 7a): the player drops
     * one of their items to make room.
     *
     * @param itemName      name of the item in the room to pick up
     * @param itemToDiscard name of the inventory item to drop into the room, or null
     */
    public PickUpItemInputData(String itemName, String itemToDiscard) {
        this.itemName = itemName;
        this.itemToDiscard = itemToDiscard;
    }

    /** @return name of the item to pick up */
    public String getItemName() {
        return itemName;
    }

    /** @return name of the item to drop, or null */
    public String getItemToDiscard() {
        return itemToDiscard;
    }
}

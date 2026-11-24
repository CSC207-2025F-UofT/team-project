package use_case.explore.pick_up_item;

import use_case.explore.ItemData;

import java.util.List;

/**
 * Output of the Pick Up Item use case: the updated room items and inventory
 * (Use Case 2, step 8).
 */
public class PickUpItemOutputData {
    /** The item that went into the inventory. */
    private final String pickedUpItemName;
    /** The item dropped to make room (extension 7a), or null. */
    private final String discardedItemName;
    /** What is left in the room. */
    private final List<ItemData> roomItems;
    /** The updated inventory. */
    private final List<ItemData> inventoryItems;
    /** Maximum inventory size. */
    private final int inventoryCapacity;

    /**
     * Creates the output data.
     *
     * @param pickedUpItemName  the item picked up
     * @param discardedItemName the item dropped, or null
     * @param roomItems         items still in the room
     * @param inventoryItems    items now carried
     * @param inventoryCapacity maximum inventory size
     */
    public PickUpItemOutputData(String pickedUpItemName, String discardedItemName,
                                List<ItemData> roomItems, List<ItemData> inventoryItems,
                                int inventoryCapacity) {
        this.pickedUpItemName = pickedUpItemName;
        this.discardedItemName = discardedItemName;
        this.roomItems = roomItems;
        this.inventoryItems = inventoryItems;
        this.inventoryCapacity = inventoryCapacity;
    }

    /** @return the item picked up */
    public String getPickedUpItemName() {
        return pickedUpItemName;
    }

    /** @return the inventory item dropped to make room, or null */
    public String getDiscardedItemName() {
        return discardedItemName;
    }

    /** @return items still in the room */
    public List<ItemData> getRoomItems() {
        return roomItems;
    }

    /** @return items now carried */
    public List<ItemData> getInventoryItems() {
        return inventoryItems;
    }

    /** @return maximum inventory size */
    public int getInventoryCapacity() {
        return inventoryCapacity;
    }
}

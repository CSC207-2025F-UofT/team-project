package use_case.explore.pick_up_item;

import java.util.List;

/**
 * Output boundary for the Pick Up Item use case. One method per possible outcome.
 */
public interface PickUpItemOutputBoundary {

    /**
     * Steps 7-8: the item is now in the inventory; show the updated inventory.
     *
     * @param outputData the updated room and inventory
     */
    void prepareSuccessView(PickUpItemOutputData outputData);

    /**
     * Extension 7a: the inventory is full. The item stays in the room, and the
     * player may choose one of their items to drop in exchange.
     *
     * @param itemName           the item the player wanted
     * @param inventoryItemNames the items the player could drop instead
     */
    void prepareInventoryFullView(String itemName, List<String> inventoryItemNames);

    /**
     * Something else went wrong (e.g. the item is no longer in the room).
     *
     * @param errorMessage message to show the player
     */
    void prepareFailView(String errorMessage);
}

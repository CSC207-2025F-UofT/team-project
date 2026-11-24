package use_case.explore.pick_up_item;

import entity.Dungeon;
import entity.Inventory;
import entity.Item;
import entity.Room;
import use_case.explore.ExploreDataAccessInterface;
import use_case.explore.ItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Use Case 2, steps 5-8 and extension 7a: save an item from the current room
 * into the player's inventory.
 *
 * <p>Step 9 (the player chooses "drop it" after reading the description) needs no
 * interactor: nothing happens, so the view simply closes the dialog.</p>
 */
public class PickUpItemInteractor implements PickUpItemInputBoundary {
    /** Where the dungeon and inventory come from. */
    private final ExploreDataAccessInterface dataAccess;
    /** Who to report results to. */
    private final PickUpItemOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess where to get the game state
     * @param presenter  where to send the results
     */
    public PickUpItemInteractor(ExploreDataAccessInterface dataAccess,
                                PickUpItemOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    /**
     * Moves an item from the current room into the inventory, handling a full
     * inventory (extension 7a).
     *
     * @param inputData which item to take, and optionally which item to drop
     */
    @Override
    public void execute(PickUpItemInputData inputData) {
        Dungeon dungeon = dataAccess.getDungeon();
        Inventory inventory = dataAccess.getInventory();
        Room room = dungeon.getCurrentRoom();

        // Make sure the item is actually in this room.
        Item item = room.findItem(inputData.getItemName());
        if (item == null) {
            presenter.prepareFailView("There is no " + inputData.getItemName() + " in this room.");
            return;
        }

        String discardName = inputData.getItemToDiscard();
        if (inventory.isFull()) {
            if (discardName == null) {
                // Extension 7a: tell the player; the item stays in the room.
                // The presenter gets the carried items so the view can ask
                // "which one do you want to drop?".
                presenter.prepareInventoryFullView(item.getName(), namesOf(inventory.getItems()));
                return;
            }
            // Extension 7a (continued): the player chose an item to drop.
            Item discard = inventory.findItem(discardName);
            if (discard == null) {
                presenter.prepareFailView("You are not carrying " + discardName + ".");
                return;
            }
            // The dropped item goes onto the room's floor, so it isn't lost.
            inventory.removeItem(discard);
            room.addItem(discard);
        }
        else {
            // There's space, so nothing needs to be dropped. Ignore any discard choice.
            discardName = null;
        }

        // Step 7: move the item from the room into the inventory, then save.
        inventory.addItem(item);
        room.removeItem(item);
        dataAccess.saveState(dungeon, inventory);

        // Step 8: present the updated inventory (and what's left in the room).
        presenter.prepareSuccessView(new PickUpItemOutputData(
                item.getName(),
                discardName,
                ItemData.fromAll(room.getItems()),
                ItemData.fromAll(inventory.getItems()),
                inventory.getMaxCapacity()));
    }

    /**
     * Collects the names of a list of items.
     *
     * @param items the items
     * @return their names, in the same order
     */
    private static List<String> namesOf(List<Item> items) {
        List<String> names = new ArrayList<>();
        for (Item i : items) {
            names.add(i.getName());
        }
        return names;
    }
}

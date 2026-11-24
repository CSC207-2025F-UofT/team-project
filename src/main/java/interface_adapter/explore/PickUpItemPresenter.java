package interface_adapter.explore;

import use_case.explore.pick_up_item.PickUpItemOutputBoundary;
import use_case.explore.pick_up_item.PickUpItemOutputData;

import java.util.List;

/**
 * Presents the result of picking up an item.
 */
public class PickUpItemPresenter implements PickUpItemOutputBoundary {
    /** The Explore screen's data. */
    private final ExploreViewModel exploreViewModel;

    /**
     * Creates the presenter.
     *
     * @param exploreViewModel the Explore screen's view model
     */
    public PickUpItemPresenter(ExploreViewModel exploreViewModel) {
        this.exploreViewModel = exploreViewModel;
    }

    /**
     * Steps 7-8: the item was picked up, so show the updated room and inventory.
     *
     * @param outputData the updated room and inventory
     */
    @Override
    public void prepareSuccessView(PickUpItemOutputData outputData) {
        ExploreState state = exploreViewModel.getState();
        // Refresh both lists: the item left the room and joined the inventory.
        state.setRoomItemNames(ExploreFormatter.names(outputData.getRoomItems()));
        state.setRoomItemDetails(ExploreFormatter.details(outputData.getRoomItems()));
        state.setInventoryItemNames(ExploreFormatter.names(outputData.getInventoryItems()));
        state.setInventoryCapacity(outputData.getInventoryCapacity());
        // The full-inventory choice (if any) is finished.
        state.setPendingItemName(null);

        // Use a different message if the player swapped an item (extension 7a).
        String message = "You put the " + outputData.getPickedUpItemName() + " in your pack.";
        if (outputData.getDiscardedItemName() != null) {
            message = "You drop the " + outputData.getDiscardedItemName()
                    + " and take the " + outputData.getPickedUpItemName() + ".";
        }
        state.setMessage(message);
        state.setError(false);
        exploreViewModel.notifyStateChanged();
    }

    /**
     * Extension 7a: the inventory is full.
     *
     * @param itemName           the item the player wanted
     * @param inventoryItemNames the items the player could drop
     */
    @Override
    public void prepareInventoryFullView(String itemName, List<String> inventoryItemNames) {
        ExploreState state = exploreViewModel.getState();
        state.setInventoryItemNames(inventoryItemNames);
        // Remember which item the player wanted, so the view can retry with it.
        state.setPendingItemName(itemName);
        state.setMessage("Your inventory is full. The " + itemName + " stays in the room.");
        state.setError(true);
        // First redraw the screen (shows the message)...
        exploreViewModel.notifyStateChanged();
        // ...then fire the special event that makes the view open the "drop what?" dialog.
        exploreViewModel.notifyInventoryFull();
    }

    /**
     * Shows an error, e.g. the item is no longer in the room.
     *
     * @param errorMessage message to show
     */
    @Override
    public void prepareFailView(String errorMessage) {
        ExploreState state = exploreViewModel.getState();
        state.setPendingItemName(null);
        state.setMessage(errorMessage);
        state.setError(true);
        exploreViewModel.notifyStateChanged();
    }
}

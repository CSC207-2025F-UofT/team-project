package interface_adapter.explore;

import use_case.explore.pick_up_item.PickUpItemInputBoundary;
import use_case.explore.pick_up_item.PickUpItemInputData;

/**
 * Turns the "Save to inventory" button (and the full-inventory dialog) into a
 * Pick Up Item request.
 */
public class PickUpItemController {
    /** The use case to call. */
    private final PickUpItemInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the Pick Up Item use case
     */
    public PickUpItemController(PickUpItemInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Picks up an item from the room (step 7).
     *
     * @param itemName the item to pick up
     */
    public void execute(String itemName) {
        interactor.execute(new PickUpItemInputData(itemName));
    }

    /**
     * Picks up an item, dropping one from the full inventory to make room (extension 7a).
     *
     * @param itemName      the item to pick up
     * @param itemToDiscard the carried item to drop
     */
    public void execute(String itemName, String itemToDiscard) {
        interactor.execute(new PickUpItemInputData(itemName, itemToDiscard));
    }
}

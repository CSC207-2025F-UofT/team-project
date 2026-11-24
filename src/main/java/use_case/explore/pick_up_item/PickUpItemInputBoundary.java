package use_case.explore.pick_up_item;

/**
 * Input boundary for saving an item from the room into the inventory
 * (Use Case 2, steps 5-8). The controller calls this interface, and
 * PickUpItemInteractor implements it.
 */
public interface PickUpItemInputBoundary {

    /**
     * Tries to pick up the item described by the input data.
     *
     * @param inputData which item to take, and optionally which item to drop
     */
    void execute(PickUpItemInputData inputData);
}

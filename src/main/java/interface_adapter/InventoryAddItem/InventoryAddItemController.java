package interface_adapter.InventoryAddItem;

import entity.User;
import entity.Item;
import use_case.InventoryAddItem.InventoryAddItemInputBoundary;
import use_case.InventoryAddItem.InventoryAddItemInputData;

/**
 * Controller for AddItem usecase
 * Gets user input
 */
public class InventoryAddItemController {
    private final InventoryAddItemInputBoundary addItemBoundary;

    public InventoryAddItemController(InventoryAddItemInputBoundary addItemBoundary) {
        this.addItemBoundary = addItemBoundary;
    }

    /**
     * @param user inventory of user to be updated
     * @param item item to be added
     */
    public void addItem(User user, Item item) {
        addItemBoundary.setUser(user);
        InventoryAddItemInputData inputData = new InventoryAddItemInputData(item);
        addItemBoundary.addItem(inputData);


    }


}

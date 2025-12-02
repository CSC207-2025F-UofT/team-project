package use_case.InventoryAddItem;

import entity.User;

/**
 * Input boundary for Add Item use case
 */

public interface InventoryAddItemInputBoundary {

    /**
     * set user
     */
    void setUser(User user);

    /**
     * To be used by Inventory_AddItem_Interactor;
     * @param inputData item to be added to inventory
     */
    void addItem(InventoryAddItemInputData inputData);
}

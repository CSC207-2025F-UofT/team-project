package use_case.InventoryUseItem;


import entity.User;

/**
 * Input Boundary for item use case
 */

public interface InventoryUseItemInputBoundary {

    /**
     * set user
     */
    void setUser(User user);
    /**
     * @param inputData called when user uses item from inventory
     */
    void useItem(InventoryUseItemInputData inputData);

    /**
     * no param. called when user wants to view inventory
     */
    void viewInventory();

}



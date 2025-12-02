package use_case.InventoryUseItem;

public interface InventoryUseItemOutputBoundary {
    /**
     * @param outputData has stat changes and updates inventory
     */
    void useItem(InventoryUseItemOutputData outputData);
    /**
     * used when User views inventory
     * @param outputData contains list of items (user inventory)
     */
    void viewInventory(InventoryUseItemOutputData outputData);
}

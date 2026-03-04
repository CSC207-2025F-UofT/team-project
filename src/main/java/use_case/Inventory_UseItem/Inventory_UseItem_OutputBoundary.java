package use_case.Inventory_UseItem;

public interface Inventory_UseItem_OutputBoundary {
    /**
     * @param outputData has stat changes and updates inventory
     */
    void useItem(Inventory_UseItem_OutputData outputData);
    /**
     * used when User views inventory
     * @param outputData contains list of items (user inventory)
     */
    void presentInventory(Inventory_UseItem_OutputData outputData);
}

package use_case.Inventory_AddItem;

import entity.Item;

public interface Inventory_InputBoundary_AddItem {

    /**
     * To be used by Inventory_AddItem_Interactor;
     * @param item item to be added to inventory
     */
    void addItem(Item item);
}

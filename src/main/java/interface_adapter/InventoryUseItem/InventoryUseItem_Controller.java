package interface_adapter.InventoryUseItem;

import use_case.Inventory_UseItem.Inventory_InputData_UseItem;
import use_case.Inventory_UseItem.Inventory_UseItem_Interactor;

public class InventoryUseItem_Controller {

private final Inventory_UseItem_Interactor interactor;

public InventoryUseItem_Controller(Inventory_UseItem_Interactor interactor) {
    this.interactor = interactor; }


// when user calls to use item
public void useItem(String itemName) {
    Inventory_InputData_UseItem inputData = new Inventory_InputData_UseItem(itemName);
    interactor.useItem(inputData); }

// when user calls to view inventory
public void viewInventory() {
    interactor.viewInventory(); }
}

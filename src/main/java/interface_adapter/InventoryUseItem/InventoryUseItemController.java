package interface_adapter.InventoryUseItem;

import entity.User;
import use_case.InventoryUseItem.InventoryUseItemInputData;
import use_case.InventoryUseItem.InventoryUseItemInputBoundary;

public class InventoryUseItemController {

private final InventoryUseItemInputBoundary useItemBoundary;
private User user;

public InventoryUseItemController(InventoryUseItemInputBoundary useItemBoundary, User user) {
    this.useItemBoundary = useItemBoundary;
    this.user = user; }

    // set the user
    public void setUser(User user) {
        this.user = user;
    }
// when user calls to use item
public void useItem(String itemName) {
    useItemBoundary.setUser(user);
    InventoryUseItemInputData inputData = new InventoryUseItemInputData(itemName);
    useItemBoundary.useItem(inputData); }


// when user calls to view inventory
public void viewInventory() {
    useItemBoundary.setUser(user);
    useItemBoundary.viewInventory(); }
}

package use_case.InventoryAddItem;

import entity.Item;
import entity.User;


public class InventoryAddItemInteractor implements InventoryAddItemInputBoundary {
private final InventoryAddItemOutputBoundary outputBoundary;
private  User user;

public InventoryAddItemInteractor(InventoryAddItemOutputBoundary outputBoundary) {
    this.outputBoundary = outputBoundary; }

/**
 * set user
 */
@Override
public void setUser(User user){
    this.user = user;
}
/**
 * Adds item to user's inventory and updates output
 * @param inputData input data is the information of the item to be added
 */

@Override
public void addItem(InventoryAddItemInputData inputData){
    if (user == null||inputData == null|| inputData.getItem() == null){return;}

    Item item = inputData.getItem();
    user.addItem(item);

    InventoryAddItemOutputData output = new InventoryAddItemOutputData(user.getInventory(), item);
    outputBoundary.present(output);}

}

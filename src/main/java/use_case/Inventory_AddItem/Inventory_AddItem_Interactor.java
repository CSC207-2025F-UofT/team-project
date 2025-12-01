package use_case.Inventory_AddItem;

import entity.Item;
import entity.Inventory;
import entity.User;
import use_case.Item.Item_Interactor;


public class Inventory_AddItem_Interactor {
private final Inventory_AddItemUserDataAccessInterface dataAccess;
private final Inventory_AddItem_OutputBoundary outputBoundary;
private final Item_Interactor itemInteractor;
private  User user;

public Inventory_AddItem_Interactor(Inventory_AddItemUserDataAccessInterface dataAccess, Inventory_AddItem_OutputBoundary outputBoundary, Item_Interactor itemInteractor) {
    this.dataAccess = dataAccess;
    this.outputBoundary = outputBoundary;
    this.itemInteractor =  itemInteractor;}

/**
 * set user
 */
public void setUser(User user){
    this.user = user;
}
/**
 * Adds item to user's inventory and updates output
 * @param inputData input data is the information of the item to be added
 */
public void addItem(Inventory_InputData_AddItem inputData){
    if (user == null){return;}
    Item item = itemInteractor.getItemByName(inputData.getItemName());
    dataAccess.addItem(user, item);
    Inventory inventory = dataAccess.getInventory(user);
    Inventory_AddItem_OutputData output = new Inventory_AddItem_OutputData(inventory, item);
    outputBoundary.present(output);}

}

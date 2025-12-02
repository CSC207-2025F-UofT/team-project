package use_case.InventoryAddItem;

import entity.Item;
import entity.Inventory;
import entity.User;

public interface InventoryAddItemUserDataAccessInterface {

    /**
     * returns inventory of user
     */
    Inventory getInventory(User user);
    /**
     * add an item
     * @param item item to be added to inventory
     * @param user to be updated with new item
     */
    void addItem(User user, Item item);


}


package use_case.Inventory_AddItem;

import entity.Item;
import entity.Inventory;
import entity.User;

public interface Inventory_AddItemUserDataAccessInterface {

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


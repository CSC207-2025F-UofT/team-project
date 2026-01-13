package use_case.Inventory_UseItem;

import entity.Item;
import entity.User;
import entity.Inventory;

public interface Inventory_UseItemUserDataAccessInterface {
    /**
     * returns inventory of user
     */
    Inventory getInventory(User user);

    /**
     * remove an item
     * @param item item to be removed from inventory
     */
    void removeItem(User user, Item item);

    /**
     *
     * @param user user of game
     * @param name name of item to be looked up
     * @return item looked up
     */
    Item getItemByName(User user, String name);
}

package use_case.InventoryUseItem;

import entity.Item;
import entity.User;
import entity.Inventory;

public interface InventoryUseItemUserDataAccessInterface {
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

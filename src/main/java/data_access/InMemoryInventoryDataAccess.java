package data_access;

import entity.Item;
import entity.User;
import use_case.Inventory.InventoryUserDataAccessInterface;
import use_case.Item.ItemUserDataAccessInterface;

import java.util.List;


public class InMemoryInventoryDataAccess implements InventoryUserDataAccessInterface{
    private  User user;
    private ItemUserDataAccessInterface itemUserDataAccess;
    public InMemoryInventoryDataAccess(User user, ItemUserDataAccessInterface itemUserDataAccess) {
        this.user = user;
        this.itemUserDataAccess = itemUserDataAccess;
        }


    @Override
    public List<Item> getInventory() {
        return user.getInventory().getItems();    }

    @Override
    public void addItem(Item item) {
        user.getInventory().addItem(item);}

    @Override
    public void removeItem(String name) {
        Item remove_item = itemUserDataAccess.getItemByName(name);
        if (remove_item != null) {
            user.getInventory().removeItem(remove_item);
        }

    }
}

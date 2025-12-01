package data_access;

import entity.Item;
import use_case.Item.ItemUserDataAccessInterface;
import java.util.ArrayList;
import java.util.List;

public class InMemoryItemDataAccess implements ItemUserDataAccessInterface {
    private final List<Item> items;

    public InMemoryItemDataAccess() {
        this.items = new ArrayList<>(); }
@Override
    public Item getItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
    return null;
    }
}

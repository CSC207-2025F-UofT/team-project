package data_access;

import entity.Inventory;
import entity.Item;
import use_case.purchase_pet_item.PurchasePetItemDataAccessInterface;
import use_case.open_pet_shop.OpenPetShopDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPetItemDataAccessObject
        implements PurchasePetItemDataAccessInterface, OpenPetShopDataAccessInterface {

    private final Map<String, Item> items = new HashMap<>();
    private final Map<String, Inventory> inventories = new HashMap<>();

    public InMemoryPetItemDataAccessObject() {
        Item food = new Item(
                "basic_food",
                "Basic Food",
                Item.Type.FOOD,
                3,
                2
        );

        Item medicine = new Item(
                "basic_medicine",
                "Basic Medicine",
                Item.Type.MEDICINE,
                5,
                3
        );

        items.put(food.getId(), food);
        items.put(medicine.getId(), medicine);
    }

    @Override
    public Item findItemById(String itemId) {
        return items.get(itemId);
    }

    @Override
    public Inventory getOrCreateInventory(String username) {
        if (!inventories.containsKey(username)) {
            inventories.put(username, new Inventory(username));
        }

        return inventories.get(username);
    }

    @Override
    public void saveInventory(Inventory inventory) {
        inventories.put(inventory.getOwnerUsername(), inventory);
    }
}
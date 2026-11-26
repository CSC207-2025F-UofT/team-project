package use_case.purchase_pet_item;

import entity.Inventory;
import entity.Item;

public interface PurchasePetItemDataAccessInterface {
    Item findItemById(String itemId);

    Inventory getOrCreateInventory(String username);

    void saveInventory(Inventory inventory);
}
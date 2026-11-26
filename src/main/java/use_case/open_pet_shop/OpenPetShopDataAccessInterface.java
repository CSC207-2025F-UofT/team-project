package use_case.open_pet_shop;

import entity.Inventory;

public interface OpenPetShopDataAccessInterface {
    Inventory getOrCreateInventory(String username);
}
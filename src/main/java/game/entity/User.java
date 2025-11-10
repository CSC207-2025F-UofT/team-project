package game.entity;

import java.util.ArrayList;

public class User {
    /*
    PetInventory: List<Pet>
    itemsList: List<Item>
    + coinCount: int
    clickBonus (Click multiplier; how many coins you get per click):int
    clickBonusTime:int
    petSlot:int
    */
    ArrayList<Pet> PetInventory = new ArrayList<Pet>();
    ArrayList<Item> ItemInventory = new ArrayList<Item>();
    int coinCount = 0;
    int clickBonus = 0;
    int slotsUnlocked = 0;

    private static final int LOOT_BOX_PRICE = 50;
    private static final int PET_FOOD_PRICE = 15;
    private static final int PET_TOY_PRICE = 20;

    public boolean buyLootBox() {
        if (coinCount >= LOOT_BOX_PRICE) {
            coinCount -= LOOT_BOX_PRICE;
            return true;
        }
        return false;

    }
    public void addToPetInventory(Pet pet) {
        if (pet != null) {
            PetInventory.add(pet);
        }
    }
    public boolean buyPetFood() {
        if (coinCount >= PET_FOOD_PRICE) {
            coinCount -= PET_FOOD_PRICE;
            Item food = new Item();
            ItemInventory.add(food);
            return true;
        }
        return false;
    }
    public boolean buyPetToy() {
        if (coinCount >= PET_TOY_PRICE) {
            coinCount -= PET_TOY_PRICE;
            Item toy = new Item();
            ItemInventory.add(toy);
            return true;
        }
        return false;
    }
}
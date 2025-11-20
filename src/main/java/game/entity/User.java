package game.entity;

import java.util.*;

public class User {
        private int coinCount;
        private int clickBonus;
        private int clickBonusTime;
        private List<Pet> PetInventory= new ArrayList<>();
        private List<Item> itemsList = new ArrayList<>();
        private int unlockedSlots;
        private String userID;
        private static final int INITIAL_COINS = 100;
        private static final int INITIAL_CLICKBONUS = 1;
        private static final int INITIAL_CLICKBONUSINCREASE = 1;

        public User() {
            this.coinCount = INITIAL_COINS;
            this.clickBonus = INITIAL_CLICKBONUS;
            this.clickBonusTime = 1;
            this.unlockedSlots = 2;
            // initialize unique user ID
        }

        public boolean coinCheck(int price) {
            return this.coinCount >= price;

        }

        private void buy(int price){
            this.coinCount -= price;
        }

        public void buyLootBox(LootBox lootBox){
            this.buy(lootBox.getPrice());
            this.addToPetInventory(lootBox.getPet());
        }

        public void buyPetFood(PetFood petFood){
            this.buy(petFood.getPrice());
            this.addToItemList(petFood);
        }

        public void buyPetToy(PetToy petToy){
            this.buy(petToy.getPrice());
            this.addToItemList(petToy);
        }

        //pre: the clickBonusTime should be less than 5
        public void upgradeClickBonus(){
            this.clickBonusTime+=1;
            this.clickBonus+=INITIAL_CLICKBONUSINCREASE;
        }

        public int getClickBonusTime(){
            return this.clickBonusTime;
        }

        //pre: the unlockPetSlot should be less than 5
        public void unlockPetSlot(){
            this.unlockedSlots+=1;
        }

        public void addToPetInventory(Pet pet) {
            this.PetInventory.add(pet);
        }
        public void addToItemList(Item item) {
            this.itemsList.add(item);
        }

        public void addCoins(int coins) {
            this.coinCount += coins;
        }

        public void subtractCoins(int coins) {
            this.coinCount -= coins;
        }

        public int getClickBonus(){
            return this.clickBonus;
        }

        public int getCoinCount(){
            return this.coinCount;
        }



}

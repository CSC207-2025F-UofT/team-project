package entity;

import java.util.List;
/**
 * Inventory Class
 * Will be used by the user to store items and also be the access point if the user wants to use items
 *
 */


public class Inventory {
    private List<Item> INVENTORY;

    public Inventory(List<Item> INVENTORY) {
        this.INVENTORY = INVENTORY; }

    // Add or remove item from inventory
    public void addItem(Item item) {
        INVENTORY.add(item); }
    public void removeItem(Item item) {
        INVENTORY.remove(item); }

    public Item getItemByName (String name){
        for(Item item : INVENTORY){
            if (item.getName().equals(name)){
                return item;
            }
        }
    return null;
    }
    public List<Item> getItems(){
        return List.copyOf(INVENTORY);
    }

    public boolean isEmpty() {
        return INVENTORY.isEmpty();
    }
}


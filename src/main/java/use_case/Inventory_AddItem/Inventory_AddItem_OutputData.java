package use_case.Inventory_AddItem;

import entity.Inventory;
import entity.Item;

/**
 * Output data for AddItem(Inventory) Use case
 * Contains updated inventory
 */
public class Inventory_AddItem_OutputData {
    private final Inventory inventory;
    private final Item item;

    public  Inventory_AddItem_OutputData(Inventory inventory, Item item) {
        this.inventory = inventory;
        this.item = item;
    }
    public Inventory getInventory() {
        return inventory;
    }
    public Item getItem() {return item;}
}

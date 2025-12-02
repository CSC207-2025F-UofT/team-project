package use_case.InventoryAddItem;

import entity.Inventory;
import entity.Item;

/**
 * Output data for AddItem(Inventory) Use case
 * Contains updated inventory
 */
public class InventoryAddItemOutputData {
    private final Inventory inventory;
    private final Item item;

    public InventoryAddItemOutputData(Inventory inventory, Item item) {
        this.inventory = inventory;
        this.item = item;
    }
    public Inventory getInventory() {
        return inventory;
    }
    public Item getItem() {return item;}
}

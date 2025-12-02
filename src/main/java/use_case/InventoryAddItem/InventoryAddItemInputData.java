package use_case.InventoryAddItem;

import entity.Item;

/**
 * When prompted to add an item to inventory,
 * the interactor will receive an item to be added to the inventory
 */
public class InventoryAddItemInputData {
    private final Item item;

    public InventoryAddItemInputData(Item item)
    {this.item = item;}

    public Item getItem()
    {return item;}
}

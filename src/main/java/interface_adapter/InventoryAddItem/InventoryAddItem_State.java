package interface_adapter.InventoryAddItem;

import entity.Item;

public class InventoryAddItem_State {
    /**
     * State object for Added Item View
     */
    private Item addedItem;

    public Item getAddedItem() {
        return addedItem; }
    public void setAddedItem(Item addedItem) {
      this.addedItem = addedItem; }
}

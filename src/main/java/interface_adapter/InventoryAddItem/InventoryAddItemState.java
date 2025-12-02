package interface_adapter.InventoryAddItem;

import entity.Item;
import entity.User;

public class InventoryAddItemState {
    /**
     * State object for Added Item View
     */
    private Item addedItem;
    private User user = null;



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user; }

    public Item getAddedItem() {
        return addedItem; }
    public void setAddedItem(Item addedItem) {
      this.addedItem = addedItem; }
}

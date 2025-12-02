package interface_adapter.InventoryUseItem;

import entity.Item;
import entity.User;

public class InventoryUseItemState {
    /**
     * State object for Added Item View
     */
    private Item useItem;
    private User user = null;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user; }

    public Item getUseItem() {
        return useItem; }
    public void setUseItem(Item useItem) {
        this.useItem = useItem; }
}

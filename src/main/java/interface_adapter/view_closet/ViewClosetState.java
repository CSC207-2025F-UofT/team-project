package interface_adapter.view_closet;

import entity.ClothingItem;
import java.util.List;

// state stores items that the closet should display
// starting with an empty list
public class ViewClosetState {
    private List<ClothingItem> items = List.of();
    public List<ClothingItem> getItems() {
        return items;
    }

    public void setItems(List<ClothingItem> items) {
        this.items = items;
    }
}

package use_case.view_closet;

import entity.ClothingItem;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * TODO: RIE
 */

public class ViewClosetOutputData {
    private final List<ClothingItem> items;

    public ViewClosetOutputData(List<ClothingItem> items) {
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    public List<ClothingItem> getItems() {
        return items;
    }
}
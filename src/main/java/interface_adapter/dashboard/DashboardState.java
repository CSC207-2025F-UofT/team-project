package interface_adapter.dashboard;

import entity.dashboard.DashboardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * State for the entire dashboard.
 */
public class DashboardState {
    private final List<DashboardItemState> itemStates = new ArrayList<>();

    public void addItemState(DashboardItemState itemState) {
        itemStates.add(itemState);
    }
    public List<DashboardItemState> getItemStates() {
        return this.itemStates;
    }
}

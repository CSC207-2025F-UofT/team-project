package use_case.dashboard;

import entity.dashboard.DashboardItem;

import java.util.List;

/**
 * Output data for new analytics outputs.
 */
public class DashboardOutputData {
    private final String title;
    private List<DashboardItem> dashboardItems;

    DashboardOutputData(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void addDashboardItem(DashboardItem dashboardItem) {
        this.dashboardItems.add(dashboardItem);
    }
    public List<DashboardItem> getDashboardItems() {
        return dashboardItems;
    }
}




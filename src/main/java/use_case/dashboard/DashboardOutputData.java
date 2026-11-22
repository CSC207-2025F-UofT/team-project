package use_case.dashboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Output data for new analytics outputs.
 */
public class DashboardOutputData {
    private final AnalyticsType analyticsType;
    private List<DashboardItem> dashboardItems;

    DashboardOutputData(AnalyticsType analyticsType) {
        this(analyticsType, new ArrayList<>());
    }
    DashboardOutputData(AnalyticsType analyticsType, List<DashboardItem> dashboardItems) {
        this.analyticsType = analyticsType;
        this.dashboardItems = dashboardItems;
    }

    public AnalyticsType getAnalyticsType() {
        return analyticsType;
    }
    public void addDashboardItem(DashboardItem dashboardItem) {
        this.dashboardItems.add(dashboardItem);
    }
    public List<DashboardItem> getDashboardItems() {
        return dashboardItems;
    }
}




package use_case.dashboard;

/**
 * Input data for the dashboard analytics requests.
 */
public class DashboardInputData {
    private final AnalyticsType analyticsType;

    public DashboardInputData(AnalyticsType analyticsType) {
        this.analyticsType = analyticsType;
    }

    public AnalyticsType getAnalyticsType() {
        return this.analyticsType;
    }
}

package interface_adapter.dashboard;

import use_case.dashboard.AnalyticsType;
import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInputData;

/**
 * Controller for the dashboard use cases.
 */
public class DashboardController {
    private final DashboardInputBoundary analyticsUseCase;

    public DashboardController(DashboardInputBoundary analyticsUseCase) {
        this.analyticsUseCase = analyticsUseCase;
    }

    public void requestAnalytics(AnalyticsType analyticsType) {
        analyticsUseCase.execute(new DashboardInputData(analyticsType));
    }
}

package interface_adapter.dashboard;

import entity.LocationInfoType;
import use_case.dashboard.AnalyticsType;
import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInputDataCatalog;


import java.util.Optional;

/**
 * Controller for the dashboard use cases.
 */
public class DashboardController {
    private final DashboardInputBoundary analyticsUseCase;

    public DashboardController(DashboardInputBoundary analyticsUseCase) {
        this.analyticsUseCase = analyticsUseCase;
    }

    public void requestAnalytics(AnalyticsType analyticsType) {
        analyticsUseCase.execute(new DashboardInputDataCatalog.MostVisitedInputData(Optional.empty()));
    }
    public void requestMostVisitedAnalytics(LocationInfoType locationInfoType) {
        analyticsUseCase.execute(new DashboardInputDataCatalog.MostVisitedInputData(Optional.ofNullable(locationInfoType)));
    }
}

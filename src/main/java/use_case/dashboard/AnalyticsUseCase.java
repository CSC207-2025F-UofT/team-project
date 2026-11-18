package use_case.dashboard;

import data_access.TripDataAccessInterface;
import entity.Trip;

/**
 * Delegate to compute analytics for the specified type, and prepare output data.
 */
public class AnalyticsUseCase implements DashboardInputBoundary {
    private final TripDataAccessInterface tripAccess;
    private final DashboardOutputBoundary presenter;

    public AnalyticsUseCase(TripDataAccessInterface tripAccess,
                         DashboardOutputBoundary dashboardOutputBoundary) {
        this.presenter = dashboardOutputBoundary;
        this.tripAccess = tripAccess;
    }
    @Override
    public void execute(DashboardInputData dashboardInputData) {
        // TODO

    }
}

package use_case.dashboard;

import use_case.list_trips.ListTripsDataAccessInterface;

/**
 * Delegate to compute analytics for the specified type, and prepare output data.
 */
public class AnalyticsUseCase implements DashboardInputBoundary {
    private final ListTripsDataAccessInterface tripAccess;
    private final DashboardOutputBoundary presenter;

    public AnalyticsUseCase(ListTripsDataAccessInterface tripAccess,
                         DashboardOutputBoundary dashboardOutputBoundary) {
        this.presenter = dashboardOutputBoundary;
        this.tripAccess = tripAccess;
    }
    @Override
    public void execute(DashboardInputData dashboardInputData) {
        // TODO

    }
}

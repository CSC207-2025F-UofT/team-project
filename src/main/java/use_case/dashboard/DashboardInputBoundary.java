package use_case.dashboard;

import data_access.TripDataAccessInterface;

public interface DashboardInputBoundary {

    /**
     * Handle dashboard analytics use case.
     *
     * @param dashboardInputData input data containing the type of analytics.
     */
    public void execute(DashboardInputData dashboardInputData);
}

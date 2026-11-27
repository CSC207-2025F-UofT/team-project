package use_case.dashboard;

/**
 * Output boundary for use cases that updates analytics dashboard.
 */
public interface DashboardOutputBoundary {

    public void prepareSuccessView(DashboardOutputData dashboardOutputData);

    public void prepareFailureView(String msg);
}

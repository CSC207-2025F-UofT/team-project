package interface_adapter.dashboard;

import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardOutputBoundary;
import use_case.dashboard.DashboardOutputData;
import view.dashboard.DashboardView;

public class DashboardPresenter implements DashboardOutputBoundary {
    private final DashboardViewModel dashboardViewModel;

    public DashboardPresenter(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
    }

    public void prepareSuccessView(DashboardOutputData dashboardOutputData) {
    }
    public void prepareFailureView(String msg) {
    }
}
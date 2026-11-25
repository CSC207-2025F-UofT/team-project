package use_case.view_dashboard;

public interface ViewDashboardOutputBoundary {

    void prepareSuccessView(ViewDashboardOutputData outputData);

    void prepareFailView(String errorMessage);
}

package use_case.manage_tasks;

public interface ManageTasksOutputBoundary {
    // successView for presenter
    void prepareSuccessView(ManageTasksOutputData output);

    // failView when an exception is thrown
    void prepareFailView(String errorMessage);
}

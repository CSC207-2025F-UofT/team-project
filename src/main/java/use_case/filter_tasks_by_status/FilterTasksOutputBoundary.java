package use_case.filter_tasks_by_status;

public interface FilterTasksOutputBoundary {
    void prepareSuccessView(FilterTasksOutputData out);
    void prepareFailView(String error);
}

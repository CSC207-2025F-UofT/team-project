package use_case.edit_task;

public interface EditTaskOutputBoundary {
    void prepareFailView(String message);
    void prepareSuccessView(EditTaskOutputData outputData);
}

package use_case.task;

public interface AddTaskOutputBoundary {
    void presentSuccess();
    void presentFailure(String errorMessage);
}

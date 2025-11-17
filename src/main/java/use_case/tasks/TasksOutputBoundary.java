package use_case.tasks;

import use_case.tasks.TasksOutputData;

public interface TasksOutputBoundary {
    void prepareSuccessView(TasksOutputData outputData);
    void prepareFailView(String error);
}

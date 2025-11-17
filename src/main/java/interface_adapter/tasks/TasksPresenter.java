package interface_adapter.tasks;

import use_case.tasks.TasksOutputBoundary;
import use_case.tasks.TasksOutputData;

public class TasksPresenter implements TasksOutputBoundary {

    private final TasksViewModel viewModel;

    public TasksPresenter(TasksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(TasksOutputData outputData) {
        // Instead of firePropertyChange, just set courses
        viewModel.setCourses(outputData.getCourses());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setError(errorMessage);
    }
}

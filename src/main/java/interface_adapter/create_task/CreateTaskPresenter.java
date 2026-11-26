package interface_adapter.create_task;

import use_case.create_task.CreateTaskOutputBoundary;
import use_case.create_task.CreateTaskOutputData;

public class CreateTaskPresenter implements CreateTaskOutputBoundary {
    private final CreateTaskViewModel viewModel;

    public CreateTaskPresenter(CreateTaskViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CreateTaskOutputData outputData) {
        viewModel.publish(outputData, "Task created.");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.publish(null, message);
    }
}
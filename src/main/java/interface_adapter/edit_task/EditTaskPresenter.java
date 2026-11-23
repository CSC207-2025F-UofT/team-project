package interface_adapter.edit_task;

import use_case.edit_task.EditTaskOutputBoundary;
import use_case.edit_task.EditTaskOutputData;

public class EditTaskPresenter implements EditTaskOutputBoundary {
    private final EditTaskViewModel viewModel;

    // Constructor.
    public EditTaskPresenter(EditTaskViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(EditTaskOutputData outputData) {
        viewModel.publish(outputData, "Task saved.");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.publish(null, message);
    }
}

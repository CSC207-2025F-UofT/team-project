package use_case.manage_tasks;

import use_case.TaskDataAccessInterface;

public class ManageTasksInteractor implements ManageTasksInputBoundary {
    private final TaskDataAccessInterface dataAccess;
    private final ManageTasksOutputBoundary presenter;

    public ManageTasksInteractor(TaskDataAccessInterface dataAccess, ManageTasksOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void addTask(ManageTasksInputData input) {

    }

    @Override
    public void editTask(ManageTasksInputData input) {

    }

    @Override
    public void deleteTask(String taskId) {

    }
}

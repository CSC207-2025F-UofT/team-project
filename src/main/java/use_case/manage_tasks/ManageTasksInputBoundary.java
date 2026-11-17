package use_case.manage_tasks;

public interface ManageTasksInputBoundary {
    void addTask(ManageTasksInputData input);

    void editTask(ManageTasksInputData input);

    void deleteTask(String taskId);
}

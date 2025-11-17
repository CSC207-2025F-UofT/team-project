package interface_adapter.tasks;

import use_case.tasks.TasksInputBoundary;
import use_case.tasks.TasksInputData;

public class TasksController {

    private final TasksInputBoundary interactor;

    public TasksController(TasksInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Called when the user clicks the Tasks button.
     */
    public void loadTasksScreen() {
        // No user-provided data yet, but we still follow InputData rules
        TasksInputData inputData = new TasksInputData();

        interactor.execute(inputData);
    }
}

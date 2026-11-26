package interface_adapter.edit_task;

import java.time.LocalDate;
import use_case.edit_task.EditTaskInputBoundary;
import use_case.edit_task.EditTaskInputData;

public class EditTaskController {
    private final EditTaskInputBoundary editTask;

    public EditTaskController(EditTaskInputBoundary editTask) {
        this.editTask = editTask;
    }

    /**
     * Submits the values from the edit form for an existing task. This method
     * builds an EditTaskInputData object and passes it to the Edit Task interactor.
     *
     * @param uniqueTaskId the ID of the existing task to update
     * @param uniqueUserId the ID of the logged-in user who owns the task
     * @param title the title shown in the form when Save is selected
     * @param description the description shown in the form, which may be empty
     * @param deadline the deadline selected for the task
     * @param estimatedHours the estimated total number of hours needed to complete the task
     * @param completionProgress the completion percentage submitted as an {@code int}.
     *                           The controller and input data preserve the original integer
     *                           so the interactor can validate that it is between 0 and 100,
     *                           inclusive, before casting it to {@code byte} for the entity.
     * @param difficultyLevel the selected difficulty (1 = Easy, 2 = Average, 3 = Difficult)
     */
    public void save(int uniqueTaskId, int uniqueUserId, String title, String description,
                     LocalDate deadline, double estimatedHours,
                     int completionProgress, byte difficultyLevel) {
        // create the input data.
        EditTaskInputData input = new EditTaskInputData(
                uniqueTaskId, uniqueUserId, title, description, deadline,
                estimatedHours, completionProgress, difficultyLevel
        );
        // this sends the input data to the EditTaskInteractor, calls the execute method in the Interactor.
        editTask.execute(input);
    }
}
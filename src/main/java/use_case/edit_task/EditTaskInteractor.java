package use_case.edit_task;

import entity.StudentTask;
import java.util.Optional;

/**
 * The EditTask Interactor.
 */
public class EditTaskInteractor implements EditTaskInputBoundary {
    private final EditTaskUserDataAccessInterface editTaskDataAccessObject;
    private final EditTaskOutputBoundary editTaskPresenter;

    public EditTaskInteractor(EditTaskUserDataAccessInterface editTaskDataAccessObject, EditTaskOutputBoundary editTaskPresenter) {
        this.editTaskDataAccessObject = editTaskDataAccessObject;
        this.editTaskPresenter = editTaskPresenter;
    }

    /**
     * The EditTaskInputData saves the StudentTask uniqueTaskId and the other attributes.
     *
     * @param editTaskInputData is the input data from user.
     */
    @Override
    public void execute(EditTaskInputData editTaskInputData) {
        int taskId = editTaskInputData.getUniqueTaskId();
        int userId = editTaskInputData.getUniqueUserId();
        Optional<StudentTask> task = editTaskDataAccessObject.findById(userId, taskId);

        // check if the task exists
        if (task.isEmpty()) {
            editTaskPresenter.prepareFailView("Task not found.");
            return;
        }

        StudentTask originalTask = task.get();

        if (editTaskInputData.getDeadline() == null){
            editTaskPresenter.prepareFailView("Deadline is required.");
            return;
        }

        double estimatedHours = editTaskInputData.getEstimatedHoursToCompleteTask();
        if (!Double.isFinite(estimatedHours) || estimatedHours <= 0) {
            editTaskPresenter.prepareFailView("Estimated hours to complete task should be finite and positive.");
            return;
        }

        int progress = editTaskInputData.getCompletionProgress();
        if (progress < 0 || progress > 100) {
            editTaskPresenter.prepareFailView("Completion progress must be an integer between 0 and 100.");
            return;
        }
        byte difficulty = editTaskInputData.getDifficultyLevel();
        if (difficulty < 1 || difficulty > 3) {
            editTaskPresenter.prepareFailView("Difficulty level is not valid.");
            return;
        }

        StudentTask updatedTask = new StudentTask(
                originalTask.getUniqueTaskId(),
                originalTask.getUniqueUserId(),
                editTaskInputData.getTitle(),
                editTaskInputData.getDescription(),
                editTaskInputData.getDeadline(),
                editTaskInputData.getEstimatedHoursToCompleteTask(),
                (byte) editTaskInputData.getCompletionProgress(),
                editTaskInputData.getDifficultyLevel()
        );
        boolean saved = editTaskDataAccessObject.update(updatedTask);

        if (! saved) {
            editTaskPresenter.prepareFailView("Task could not be updated.");
            return;
        }

        EditTaskOutputData outputData = new EditTaskOutputData(updatedTask.getUniqueTaskId(), updatedTask.getUniqueUserId(),
                updatedTask.getTitle(),updatedTask.getDescription(), updatedTask.getDeadline(), updatedTask.getEstimatedHoursToCompleteTask(),
                updatedTask.getCompletionProgress(), updatedTask.getDifficultyLevel());
        editTaskPresenter.prepareSuccessView(outputData);
    }

}

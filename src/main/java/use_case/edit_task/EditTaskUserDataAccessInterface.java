package use_case.edit_task;

import entity.StudentTask;
import java.util.Optional;

public interface EditTaskUserDataAccessInterface {

    /**
     * Gets the specific StudentTask Object with the corresponding uniqueUserId and uniqueTaskId.
     */
    Optional<StudentTask> findById(int uniqueUserId, int uniqueTaskId);

    /**
     * Updates the specific StudentTask, which finds the task by using the .getUniqueUserId() and .getUniqueTaskId()
     * for the updated task, and store the data.
     */
    boolean update(StudentTask updatedTask);
}

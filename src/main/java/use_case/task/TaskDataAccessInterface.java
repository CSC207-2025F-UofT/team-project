package use_case.task;

import entity.Task;
import java.util.List;

public interface TaskDataAccessInterface {
    void saveTask(Task task);
    List<Task> getAllTasks();
}

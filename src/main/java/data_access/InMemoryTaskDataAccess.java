package data_access;

import entity.StudentTask;
import use_case.edit_task.EditTaskUserDataAccessInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryTaskDataAccess implements EditTaskUserDataAccessInterface{
    private final Map<Integer, Map<Integer, StudentTask>> tasksByUser = new HashMap<>();

    public InMemoryTaskDataAccess(List<StudentTask> initialTasks) {
        for (StudentTask task : initialTasks) {
            int userId = task.getUniqueUserId();
            int taskId = task.getUniqueTaskId();

            Map<Integer, StudentTask> userTasks = tasksByUser.get(userId);

            if (userTasks == null) {
                userTasks = new HashMap<>();
                tasksByUser.put(userId, userTasks);
            }

            userTasks.put(taskId, task);
        }
    }

    @Override
    public Optional<StudentTask> findById(int userId, int taskId) {
        Map<Integer, StudentTask> userTasks = tasksByUser.get(userId);

        if (userTasks == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(userTasks.get(taskId));
    }

    @Override
    public boolean update(StudentTask updatedTask) {
        int userId = updatedTask.getUniqueUserId();
        int taskId = updatedTask.getUniqueTaskId();

        Map<Integer, StudentTask> userTasks = tasksByUser.get(userId);

        if (userTasks == null || !userTasks.containsKey(taskId)) {
            return false;
        }

        userTasks.put(taskId, updatedTask);
        return true;
    }
}
package data_access;

import entity.Task;
import use_case.task.TaskDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskDataAccess implements TaskDataAccessInterface {

    private List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> loadTasksForUser(String email) {
        return new ArrayList<>(tasks);
    }

    @Override
    public void saveTasksForUser(String email, List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }
}

package entity;
import java.util.ArrayList;
import java.util.List;

public abstract class TasksUser extends User {
    private final List<Task> tasks;

    public TasksUser(String username, String password, int uniqueUserId) {
        super(username, password, uniqueUserId);
        this.tasks = new ArrayList<>();
    }

    public TasksUser(String username, String password, int uniqueUserId, List<Task> tasks) {
        super(username, password, uniqueUserId);
        this.tasks = tasks != null ? new ArrayList<>(tasks) : new ArrayList<>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        if (task != null) {
            this.tasks.add(task);
        }
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
    }
}

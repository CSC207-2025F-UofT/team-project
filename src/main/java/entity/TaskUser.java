package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TaskUser<T extends Task> extends User {
    private final List<T> tasks;

    public TaskUser(String username, String password, int uniqueUserId) {
        super(username, password, uniqueUserId);
        this.tasks = new ArrayList<>();
    }

    public TaskUser(String username, String password, int uniqueUserId,
                    List<T> tasks) {
        this(username, password, uniqueUserId);

        if (tasks != null) {
            for (T task : tasks) {
                validateTask(task);
                this.tasks.add(task);
            }
        }
    }

    public List<T> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public T getTask(int uniqueTaskId) {
        for (T task : tasks) {
            if (task.getUniqueTaskId() == uniqueTaskId) {
                return task;
            }
        }
        return null;
    }

    public void addTask(T task) {
        validateTask(task);
        this.tasks.add(task);
    }

    public void removeTask(T task) {
        if (task != null && task.getUniqueUserId() == getUniqueUserId()) {
            this.tasks.remove(task);
        }
    }

    private void validateTask(T task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }

        if (task.getUniqueUserId() != super.getUniqueUserId()) {
            throw new IllegalArgumentException(
                    "Task must belong to the current user.");
        }

        for (T existingTask : tasks) {
            if (existingTask.getUniqueTaskId() == task.getUniqueTaskId()) {
                throw new IllegalArgumentException(
                        "A task with the same uniqueTaskId already exists.");
            }
        }
    }
}
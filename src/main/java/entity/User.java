package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a user of the LockIn application.
 */
public class User {

    private final int id;          // unique identifier
    private String name;           // user's name
    private String email;          // user's email address

    // Tasks owned by this user
    private final List<Task> tasks = new ArrayList<>();

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // --- Getters ---

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Returns an unmodifiable view of the user's tasks.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    // --- Mutating methods ---

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("task cannot be null");
        }
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    /**
     * Removes all tasks.
     */
    public void clearTasks() {
        tasks.clear();
    }
}

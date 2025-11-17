package entity;

import java.util.Objects;

public class Task {
    private final String id;
    private String title;
    private boolean completed;

    public Task(String id, String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        this.id = Objects.requireNonNull(id);
        this.title = title;
        this.completed = false;
    }

    public String getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        this.title = title;
    }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}

package entity;

import java.time.LocalDateTime;

// One item on student's to-do list
// Immutable: editing a task means saving a new Task with the same id

public class Task {
    private final String id;
    private final String title;
    private final int estimatedMinutes;
    private final LocalDateTime deadline;

    public Task(String id, String title, int estimatedMinutes, LocalDateTime deadline) {
        if (title == null) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        if (estimatedMinutes <= 0) {
            throw new IllegalArgumentException("Estimated time must be positive");
        }
        this.id = id;
        this.title = title.strip();
        this.estimatedMinutes = estimatedMinutes;
        this.deadline = deadline;
    }

    // getters
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }
    // returns null if the student didn't set a deadline
    public LocalDateTime getDeadline() {
        return deadline;
    }
}

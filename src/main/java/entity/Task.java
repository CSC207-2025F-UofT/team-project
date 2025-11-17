package entity;

import java.time.LocalDate;

/**
 * Represents a single task or calendar item in the LockIn application.
 * Tasks include assignments, tests, events, reminders, or any dated item
 * the user wants to track. This unified model allows tasks to naturally
 * appear in both the to-do list and the calendar view.
 */
public class Task {

    private final int id;              // unique identifier
    private String title;              // e.g. "Math Assignment 3"
    private String description;        // optional details
    private LocalDate date;            // date of the task/event
    private String type;               // e.g. "assignment", "test", "event"
    private String course;             // e.g. "CSC207", "MAT137", may be null
    private boolean completed;         // true if finished

    /**
     * Main constructor for a Task, including course.
     */
    public Task(int id,
                String title,
                String description,
                LocalDate date,
                String type,
                String course) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
        this.course = course;
        this.completed = false;
    }

    /**
     * Convenience constructor without course (kept for compatibility).
     * Sets course to null.
     */
    public Task(int id,
                String title,
                String description,
                LocalDate date,
                String type) {
        this(id, title, description, date, type, null);
    }

    // --- Getters ---

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the date associated with this task.
     * Used by the Calendar view to place tasks on the correct day.
     */
    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getCourse() {
        return course;
    }

    public boolean isCompleted() {
        return completed;
    }

    // --- Setters / Mutating methods ---

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public void markIncomplete() {
        this.completed = false;
    }

    /**
     * Helper method: checks if the task is overdue compared to the given date.
     */
    public boolean isOverdue(LocalDate today) {
        if (date == null) return false;
        return !completed && date.isBefore(today);
    }
}

package use_case.manage_tasks;

import java.time.LocalDateTime;

public class TaskData {
    private final String id;
    private final String title;
    private final int estimatedMinutes;
    private final LocalDateTime deadline; // null when there's no deadline

    public TaskData(String id, String title, int estimatedMinutes, LocalDateTime deadline) {
        this.id = id;
        this.title = title;
        this.estimatedMinutes = estimatedMinutes;
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }
}


package use_case.manage_tasks;

import java.time.LocalDateTime;

public class ManageTasksInputData {

    private final String taskId;
    private final String title;
    private final int estimatedMinutes;
    private final LocalDateTime deadline;

    public ManageTasksInputData(String taskId, String title, int estimatedMinutes, LocalDateTime deadline) {
        this.taskId = taskId;
        this.title = title;
        this.estimatedMinutes = estimatedMinutes;
        this.deadline = deadline;
    }

    // getters
    public String getTaskId() {
        return taskId;
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

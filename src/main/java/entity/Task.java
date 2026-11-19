package entity;

import java.time.LocalDate;

public class Task {
    private final int uniqueTaskId;
    private final int uniqueUserId;
    private String title;
    private LocalDate deadline;
    private byte completionProgress;

    public Task(int uniqueTaskId, int uniqueUserId, String title, LocalDate deadline, byte completionProgress) {
        this.uniqueTaskId = uniqueTaskId;
        this.uniqueUserId = uniqueUserId;
        this.title = title;
        this.deadline = deadline;
        this.completionProgress = completionProgress;
    }

    public Task(String title, LocalDate deadline, byte completionProgress) {
        this(0, 0, title, deadline, completionProgress);
    }

    public int getUniqueTaskId() {
        return uniqueTaskId;
    }

    public int getUniqueUserId() {
        return uniqueUserId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public byte getCompletionProgress() {
        return completionProgress;
    }
}

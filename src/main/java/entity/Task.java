package entity;

import java.time.LocalDate;

public class Task {
    private String title;
    private LocalDate deadline;
    private byte completionProgress;

    public Task(String title, LocalDate deadline, byte completionProgress) {
        this.title = title;
        this.deadline = deadline;
        this.completionProgress = completionProgress;
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

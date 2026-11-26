package use_case.create_task;

import java.time.LocalDate;

public class CreateTaskOutputData {
    private final int taskId;
    private final String title;
    private final LocalDate deadline;
    private final double estimatedHours;
    private final String difficultyDisplayName;
    private final byte completionProgress;

    public CreateTaskOutputData(
            int taskId,
            String title,
            LocalDate deadline,
            double estimatedHours,
            String difficultyDisplayName,
            byte completionProgress
    ) {
        this.taskId = taskId;
        this.title = title;
        this.deadline = deadline;
        this.estimatedHours = estimatedHours;
        this.difficultyDisplayName = difficultyDisplayName;
        this.completionProgress = completionProgress;
    }

    public int getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public LocalDate getDeadline() { return deadline; }
    public double getEstimatedHours() { return estimatedHours; }
    public String getDifficultyDisplayName() { return difficultyDisplayName; }
    public byte getCompletionProgress() { return completionProgress; }
}
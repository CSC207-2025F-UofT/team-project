package use_case.edit_task;

import java.time.LocalDate;

public class EditTaskInputData {

    private final int uniqueTaskId;
    private final int uniqueUserId;
    private final String title;
    private final String description;
    private final LocalDate deadline;
    private final double estimatedHoursToCompleteTask;
    private final int completionProgress;
    private final byte difficultyLevel;

    public EditTaskInputData(int uniqueTaskId, int uniqueUserId, String title, String description, LocalDate deadline, double estimatedHoursToCompleteTask, int completionProgress, byte difficultyLevel) {
        this.uniqueTaskId = uniqueTaskId;
        this.uniqueUserId = uniqueUserId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.estimatedHoursToCompleteTask = estimatedHoursToCompleteTask;
        this.completionProgress = completionProgress;
        this.difficultyLevel = difficultyLevel;
    }

    public int getUniqueTaskId() {
        return this.uniqueTaskId;
    }
    public int getUniqueUserId(){
        return this.uniqueUserId;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDescription(){
        return this.description;
    }
    public LocalDate getDeadline(){
        return this.deadline;
    }
    public double getEstimatedHoursToCompleteTask(){
        return this.estimatedHoursToCompleteTask;
    }
    public int getCompletionProgress(){
        return this.completionProgress;
    }
    public byte getDifficultyLevel(){
        return this.difficultyLevel;
    }

}

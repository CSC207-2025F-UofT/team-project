package entity;

import java.time.LocalDate;

public class StudentTask extends Task{

    private String title;
    private String description;
    private LocalDate deadline;
    private double estimatedHoursToCompleteTask;
    private byte completionProgress;
    private byte difficultyLevel;

    /*
    ** Creates a StudentTask object with all the required arguments filled in.
     */
    public StudentTask(int uniqueTaskId, int uniqueUserId, String title, String description, LocalDate deadline, double estimatedHoursToCompleteTask, byte completionProgress, byte difficultyLevel) {
        super(uniqueTaskId, uniqueUserId);
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.estimatedHoursToCompleteTask = estimatedHoursToCompleteTask;
        this.completionProgress = completionProgress;
        this.difficultyLevel = difficultyLevel;
    }

    /*
    ** Check if all fields entered are valid, if not, throw an error.
     */
    private boolean fieldsValidation(String title, String description, LocalDate deadline, byte completionProgress) {
        return !(title == null | description == null | deadline == null | (completionProgress < 0 | completionProgress > 100));
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

    public byte getCompletionProgress(){
        return this.completionProgress;
    }

    public byte getDifficultyLevel(){
        return this.difficultyLevel;
    }
}

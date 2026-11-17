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
        this.title = title == null ? "" : title;
        this.description = description;
        this.deadline = deadline;
        this.estimatedHoursToCompleteTask = estimatedHoursToCompleteTask;
        this.completionProgress = completionProgress;
        this.difficultyLevel = difficultyLevel;
        boolean valid = fieldsValidation();
        if (!valid) {
            throw new IllegalArgumentException("The StudentTask has invalid fields.");
        }
    }

    public void setTitle(String newTitle) {
        this.title = newTitle == null ? "" : newTitle;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setDeadline(LocalDate newDeadline) {
        if (!fieldsValidation()) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        LocalDate previousDeadline = this.deadline;
        this.deadline = newDeadline;

        if (!fieldsValidation()) {
            this.deadline = previousDeadline;
            throw new IllegalArgumentException("The new deadline is not valid.");
        }

    }

    public void setEstimatedHoursToCompleteTask(double newEstimatedHoursToCompleteTask) {
        if (!fieldsValidation()) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        double previousEstimatedHours = this.estimatedHoursToCompleteTask;
        this.estimatedHoursToCompleteTask = newEstimatedHoursToCompleteTask;

        if (!fieldsValidation()) {
            this.estimatedHoursToCompleteTask = previousEstimatedHours;
            throw new IllegalArgumentException("The new estimated hours to complete task is not valid, it should be a double greater than 0.");
        }
    }

    public void setCompletionProgress(byte newCompletionProgress) {
        if (!fieldsValidation()) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        byte previousCompletionProgress = this.completionProgress;
        this.completionProgress = newCompletionProgress;

        if (!fieldsValidation()) {
            this.completionProgress = previousCompletionProgress;
            throw new IllegalArgumentException("The new completion progress is not valid, it should be between 0-100.");
        }
    }

    public void setDifficultyLevel(byte newDifficultyLevel){
        this.difficultyLevel = newDifficultyLevel;
    }

    /*
    ** Check if all fields entered are valid, if not, throw an error.
     */
    public boolean fieldsValidation() {
        return inputValidation(
                getUniqueUserId(),
                this.deadline,
                this.estimatedHoursToCompleteTask,
                this.completionProgress
        );
    }

    /*
     * Checks if the input provided by the user is valid.
     */
    public static boolean inputValidation(int userId, LocalDate deadline, double estimatedHours, int progress) {
        return userId != 0
                && deadline != null
                && Double.isFinite(estimatedHours)
                && estimatedHours > 0
                && progress >= 0
                && progress <= 100;
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

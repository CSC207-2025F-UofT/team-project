package entity;

import java.time.LocalDate;

/**
 * Represents a Student's Task.
 * <p>
 * Class Invariants:
 *   The owning user's ID is nonzero.
 *   The deadline is non-null and may be in the past.
 *   The estimated hours are finite and greater than zero.
 *   The completion progress is between 0 and 100, inclusive.
 *   The title is not null. An empty title is allowed.
 *
 */
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

    /**
     * Update the deadline. Past dates are allowed for this.
     *
     * @param newDeadline is the new deadline for this task, newDeadline should not be null.
     * @throws IllegalArgumentException if the new deadline is null.
     */
    public void setDeadline(LocalDate newDeadline) {
        if (!fieldsValidation() || !inputValidation(this.getUniqueUserId(), newDeadline, this.estimatedHoursToCompleteTask, this.completionProgress)) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        LocalDate previousDeadline = this.deadline;
        this.deadline = newDeadline;

        if (!fieldsValidation()) {
            this.deadline = previousDeadline;
            throw new IllegalArgumentException("The new deadline is not valid.");
        }

    }

    /**
     * Update the estimated hours to complete this task.
     *
     * @param newEstimatedHoursToCompleteTask is the new estimated hour. This is a double that must be finite and positive.
     * @throws IllegalArgumentException if the new estimated hour is not finite or is not positive.
     */
    public void setEstimatedHoursToCompleteTask(double newEstimatedHoursToCompleteTask) {
        if (!fieldsValidation() || !inputValidation(this.getUniqueUserId(), this.getDeadline(), newEstimatedHoursToCompleteTask, this.completionProgress)) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        double previousEstimatedHours = this.estimatedHoursToCompleteTask;
        this.estimatedHoursToCompleteTask = newEstimatedHoursToCompleteTask;

        if (!fieldsValidation()) {
            this.estimatedHoursToCompleteTask = previousEstimatedHours;
            throw new IllegalArgumentException("The new estimated hours to complete task is not valid, it should be a double greater than 0.");
        }
    }

    /**
     * Update the completion progress of this task.
     *
     * @param newCompletionProgress is the new completion progress for this task. This should be between 0 and 100, inclusively.
     * @throws IllegalArgumentException if the updated information is less than 0 or greater than 100.
     */
    public void setCompletionProgress(byte newCompletionProgress) {
        if (!fieldsValidation() || !inputValidation(this.getUniqueUserId(), this.getDeadline(), this.getEstimatedHoursToCompleteTask(), newCompletionProgress)) {
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

    /**
     * Check if all fields entered are valid, if not, throw an error.
     * The title and the description cannot be null, but are allowed to be empty string.
     */
    public boolean fieldsValidation() {
        return inputValidation(
                getUniqueUserId(),
                this.deadline,
                this.estimatedHoursToCompleteTask,
                this.completionProgress
        )
                && this.title != null
                && this.description != null;
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

package entity;

import java.time.LocalDate;

/** Represents a student's task. */
public class StudentTask extends Task {
    public static final double DEFAULT_ESTIMATED_HOURS = 10.0;
    public static final DifficultyLevel DEFAULT_DIFFICULTY_LEVEL = DifficultyLevel.AVERAGE;

    private String title;
    private String description;
    private LocalDate deadline;
    private double estimatedHoursToCompleteTask;
    private byte completionProgress;
    private DifficultyLevel difficultyLevel;

    public StudentTask(int uniqueTaskId, int uniqueUserId, String title,
                       String description, LocalDate deadline) {
        this(uniqueTaskId, uniqueUserId, title, description, deadline,
                DEFAULT_ESTIMATED_HOURS, (byte) 0, DEFAULT_DIFFICULTY_LEVEL);
    }

    public StudentTask(int uniqueTaskId, int uniqueUserId, String title,
                       String description, LocalDate deadline,
                       double estimatedHoursToCompleteTask, byte completionProgress,
                       DifficultyLevel difficultyLevel) {
        super(uniqueTaskId, uniqueUserId);
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.estimatedHoursToCompleteTask = estimatedHoursToCompleteTask;
        this.completionProgress = completionProgress;
        this.difficultyLevel = difficultyLevel;

        if (!fieldsValidation()) {
            throw new IllegalArgumentException("The StudentTask has invalid fields.");
        }
    }

    public boolean fieldsValidation() {
        return getUniqueUserId() != 0
                && title != null
                && description != null
                && difficultyLevel != null
                && inputValidation(deadline, estimatedHoursToCompleteTask, completionProgress);
    }

    public boolean inputValidation(LocalDate deadline, double estimatedHours, byte progress) {
        return deadline != null
                && Double.isFinite(estimatedHours)
                && estimatedHours > 0
                && progress >= 0
                && progress <= 100;
    }

    public void setTitle(String newTitle) {
        if (!fieldsValidation()) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        String previousTitle = this.title;
        this.title = newTitle;

        if (!fieldsValidation()) {
            this.title = previousTitle;
            throw new IllegalArgumentException("The new title cannot be null.");
        }
    }

    public void setDescription(String newDescription) {
        if (!fieldsValidation()) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        String previousDescription = this.description;
        this.description = newDescription;

        if (!fieldsValidation()) {
            this.description = previousDescription;
            throw new IllegalArgumentException("The new description cannot be null.");
        }
    }

    /** Past deadlines are allowed, but null deadlines are not. */
    public void setDeadline(LocalDate newDeadline) {
        if (!fieldsValidation()) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        if (!inputValidation(newDeadline, estimatedHoursToCompleteTask, completionProgress)) {
            throw new IllegalArgumentException("The new deadline is not valid.");
        }

        LocalDate previousDeadline = this.deadline;
        this.deadline = newDeadline;

        if (!fieldsValidation()) {
            this.deadline = previousDeadline;
            throw new IllegalArgumentException("The new deadline is not valid.");
        }
    }

    public void setEstimatedHoursToCompleteTask(double newEstimatedHours) {
        if (!fieldsValidation()) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        if (!inputValidation(deadline, newEstimatedHours, completionProgress)) {
            throw new IllegalArgumentException("Estimated hours must be finite and greater than 0.");
        }

        double previousEstimatedHours = this.estimatedHoursToCompleteTask;
        this.estimatedHoursToCompleteTask = newEstimatedHours;

        if (!fieldsValidation()) {
            this.estimatedHoursToCompleteTask = previousEstimatedHours;
            throw new IllegalArgumentException("Estimated hours must be finite and greater than 0.");
        }
    }

    public void setCompletionProgress(byte newCompletionProgress) {
        if (!fieldsValidation()) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        if (!inputValidation(deadline, estimatedHoursToCompleteTask, newCompletionProgress)) {
            throw new IllegalArgumentException("Completion progress must be between 0 and 100.");
        }

        byte previousCompletionProgress = this.completionProgress;
        this.completionProgress = newCompletionProgress;

        if (!fieldsValidation()) {
            this.completionProgress = previousCompletionProgress;
            throw new IllegalArgumentException("Completion progress must be between 0 and 100.");
        }
    }

    public void setDifficultyLevel(DifficultyLevel newDifficultyLevel) {
        if (!fieldsValidation()) {
            throw new IllegalStateException("The task has invalid fields.");
        }
        DifficultyLevel previousDifficultyLevel = this.difficultyLevel;
        this.difficultyLevel = newDifficultyLevel;

        if (!fieldsValidation()) {
            this.difficultyLevel = previousDifficultyLevel;
            throw new IllegalArgumentException("The difficulty level cannot be null.");
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public double getEstimatedHoursToCompleteTask() {
        return estimatedHoursToCompleteTask;
    }

    public byte getCompletionProgress() {
        return completionProgress;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
}
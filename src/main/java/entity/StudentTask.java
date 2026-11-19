package entity;

import java.time.LocalDate;

public class StudentTask extends Task {
    public static final double DEFAULT_ESTIMATED_HOURS = 10.0;
    public static final byte DEFAULT_DIFFICULTY_LEVEL = 1; // 0 = Easy, 1 = Average, 2 = Difficult

    private String description;
    private double estimatedHoursToCompleteTask;
    private byte difficultyLevel;
    private String courseCode;

    public StudentTask(int uniqueTaskId, int uniqueUserId, String title, String description, LocalDate deadline) {
        this(uniqueTaskId, uniqueUserId, title, description, deadline,
                DEFAULT_ESTIMATED_HOURS, (byte) 0, DEFAULT_DIFFICULTY_LEVEL, null);
    }

    public StudentTask(int uniqueTaskId, int uniqueUserId, String title, String description, LocalDate deadline,
                       double estimatedHoursToCompleteTask, byte completionProgress,
                       byte difficultyLevel, String courseCode) {
        super(uniqueTaskId, uniqueUserId, title, deadline, completionProgress);
        this.description = description;
        this.estimatedHoursToCompleteTask = estimatedHoursToCompleteTask;
        this.difficultyLevel = difficultyLevel;
        this.courseCode = courseCode;
    }

    public boolean fieldsValidation() {
        boolean validTitle = getTitle() != null && !getTitle().isBlank();
        boolean validDeadline = getDeadline() != null;
        boolean validHours = !Double.isNaN(estimatedHoursToCompleteTask)
                && !Double.isInfinite(estimatedHoursToCompleteTask)
                && estimatedHoursToCompleteTask > 0;
        boolean validProgress = getCompletionProgress() >= 0 && getCompletionProgress() <= 100;
        boolean validDifficulty = difficultyLevel >= 0 && difficultyLevel <= 2;

        return validTitle && validDeadline && validHours && validProgress && validDifficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getEstimatedHoursToCompleteTask() {
        return estimatedHoursToCompleteTask;
    }

    public void setEstimatedHoursToCompleteTask(double estimatedHoursToCompleteTask) {
        this.estimatedHoursToCompleteTask = estimatedHoursToCompleteTask;
    }

    public byte getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(byte difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
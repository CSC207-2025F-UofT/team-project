package use_case.create_task;

import entity.DifficultyLevel;
import java.time.LocalDate;

public class CreateTaskInputData {
    private final int userId;
    private final String title;
    private final String description;
    private final LocalDate deadline;
    private final Double estimatedHours; // nullable so default (10.0) can be applied
    private final DifficultyLevel difficultyLevel; // nullable so default (AVERAGE) can be applied

    public CreateTaskInputData(
            int userId,
            String title,
            String description,
            LocalDate deadline,
            Double estimatedHours,
            DifficultyLevel difficultyLevel
    ) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.estimatedHours = estimatedHours;
        this.difficultyLevel = difficultyLevel;
    }

    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public Double getEstimatedHours() { return estimatedHours; }
    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
}
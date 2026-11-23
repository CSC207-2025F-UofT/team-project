package use_case.create_task;

import entity.DifficultyLevel;
import entity.StudentTask;
import java.time.LocalDate;

public class CreateTaskInteractor implements CreateTaskInputBoundary {
    private static final double DEFAULT_ESTIMATED_HOURS = 10.0;
    private static final DifficultyLevel DEFAULT_DIFFICULTY = DifficultyLevel.AVERAGE;
    private static final byte INITIAL_PROGRESS = 0;

    private final CreateTaskUserDataAccessInterface userDataAccess;
    private final CreateTaskOutputBoundary presenter;

    public CreateTaskInteractor(
            CreateTaskUserDataAccessInterface userDataAccess,
            CreateTaskOutputBoundary presenter
    ) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(CreateTaskInputData inputData) {
        if (!userDataAccess.existsByUserId(inputData.getUserId())) {
            presenter.prepareFailView("Logged-in user not found.");
            return;
        }

        String title = inputData.getTitle() == null ? "" : inputData.getTitle().trim();
        String description = inputData.getDescription() == null ? "" : inputData.getDescription().trim();
        LocalDate deadline = inputData.getDeadline();

        if (title.isEmpty()) {
            presenter.prepareFailView("Task title cannot be empty.");
            return;
        }

        if (deadline == null) {
            presenter.prepareFailView("Task deadline is missing or invalid.");
            return;
        }

        double estimatedHours = inputData.getEstimatedHours() != null
                ? inputData.getEstimatedHours()
                : DEFAULT_ESTIMATED_HOURS;

        if (estimatedHours <= 0 || Double.isNaN(estimatedHours) || Double.isInfinite(estimatedHours)) {
            presenter.prepareFailView("Estimated hours must be a positive number.");
            return;
        }

        DifficultyLevel difficulty = inputData.getDifficultyLevel() != null
                ? inputData.getDifficultyLevel()
                : DEFAULT_DIFFICULTY;

        int taskId = userDataAccess.generateNextTaskId();

        // Adjust constructor arguments to match your exact StudentTask constructor signature
        StudentTask newTask = new StudentTask(
                taskId,
                inputData.getUserId(),
                title,
                description,
                deadline,
                estimatedHours,
                INITIAL_PROGRESS,
                difficulty
        );

        userDataAccess.saveTask(inputData.getUserId(), newTask);

        CreateTaskOutputData outputData = new CreateTaskOutputData(
                taskId,
                title,
                deadline,
                estimatedHours,
                difficulty.getDisplayName(),
                INITIAL_PROGRESS
        );
        presenter.prepareSuccessView(outputData);
    }
}
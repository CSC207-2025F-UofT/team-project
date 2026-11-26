package interface_adapter.create_task;

import entity.DifficultyLevel;
import java.time.LocalDate;
import use_case.create_task.CreateTaskInputBoundary;
import use_case.create_task.CreateTaskInputData;

public class CreateTaskController {
    private final CreateTaskInputBoundary createTaskInteractor;

    public CreateTaskController(CreateTaskInputBoundary createTaskInteractor) {
        this.createTaskInteractor = createTaskInteractor;
    }

    /**
     * Submits the values from the create form for a new task. Builds a
     * {@code CreateTaskInputData} object and passes it to the Create Task interactor.
     *
     * @param userId         the ID of the logged-in user creating the task
     * @param title          the task title from the form
     * @param description    the task description, which may be empty or null
     * @param deadline       the selected deadline for the task
     * @param estimatedHours the estimated hours needed, or {@code null} to use the interactor default
     * @param difficulty     the selected {@link DifficultyLevel}, or {@code null} to use the interactor default
     */
    public void create(int userId, String title, String description,
                       LocalDate deadline, Double estimatedHours, DifficultyLevel difficulty) {
        CreateTaskInputData inputData = new CreateTaskInputData(
                userId, title, description, deadline, estimatedHours, difficulty
        );
        createTaskInteractor.execute(inputData);
    }
}
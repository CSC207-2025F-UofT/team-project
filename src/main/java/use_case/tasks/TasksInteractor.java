package use_case.tasks;

import use_case.tasks.TasksOutputBoundary;
import interface_adapter.tasks.dto.CourseDTO;

import java.util.ArrayList;
import java.util.List;

public class TasksInteractor implements TasksInputBoundary {

    private final TasksOutputBoundary presenter;
    private final TasksDataAccessInterface dataAccess;

    public TasksInteractor(TasksOutputBoundary presenter, TasksDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(TasksInputData inputData) {
        try {
            // Fetch courses and tasks from data access
            List<CourseDTO> courses = dataAccess.getCourses();

            // Wrap in output data
            TasksOutputData outputData = new TasksOutputData(courses);

            // Send to presenter
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Failed to load tasks: " + e.getMessage());
        }
    }
}

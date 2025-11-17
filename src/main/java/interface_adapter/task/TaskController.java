package interface_adapter.task;

import use_case.task.AddTaskInputBoundary;
import use_case.task.AddTaskInputData;

import java.time.LocalDate;

public class TaskController {

    private final AddTaskInputBoundary interactor;

    public TaskController(AddTaskInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addTask(String title, String description, LocalDate date, String type) {
        AddTaskInputData input = new AddTaskInputData(title, description, date, type);
        interactor.add(input);
    }
}

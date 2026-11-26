package app;

import data_access.InMemoryTaskDataAccess;
import entity.StudentTask;
import interface_adapter.ViewManagerModel;
import interface_adapter.edit_task.EditTaskController;
import interface_adapter.edit_task.EditTaskPresenter;
import interface_adapter.edit_task.EditTaskViewModel;
import interface_adapter.welcome.WelcomeViewModel;
import use_case.edit_task.EditTaskInteractor;
import use_case.edit_task.EditTaskOutputData;
import view.TaskListView;
import view.ViewManager;
import view.WelcomeView;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AppBuilder {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private static final String SAMPLE_TASKS = "sample tasks";

    public AppBuilder() {
        new ViewManager(cardPanel, cardLayout, viewManagerModel);
        WelcomeView welcome = new WelcomeView(new WelcomeViewModel());

        // TODO: This Open sample tasks is just for demo and testing, it should be removed after the user is
        //  able to enter the actual task lists.
        JButton openTasks = new JButton("Open sample tasks");
        openTasks.addActionListener(event -> show(SAMPLE_TASKS));
        welcome.add(openTasks);

        // TODO: This should be kept, since we have a welcome page at the beginning of the app.
        cardPanel.add(welcome, WelcomeViewModel.VIEW_NAME);

        // This branch has no login or persistent task list yet.
        int sampleUserId = 1;
        LocalDate today = LocalDate.now();
        List<StudentTask> samples = List.of(
                new StudentTask(1, sampleUserId, "Finish CSC207 MVP", "Review the edit flow",
                        today.plusDays(7), 10, (byte) 20, (byte) 2),
                new StudentTask(2, sampleUserId, "Write unit tests", "", today.plusDays(3),
                        3, (byte) 0, (byte) 1));

        // TODO: The DAO should be replaced with the real DAO after it is implemented.
        InMemoryTaskDataAccess dao = new InMemoryTaskDataAccess(samples);

        // TODO: These should be kept: viewModel, presenter, controller, interactor for editTask use case.
        EditTaskViewModel editTaskViewModel = new EditTaskViewModel();
        EditTaskPresenter editTaskPresenter = new EditTaskPresenter(editTaskViewModel);
        EditTaskInteractor editTaskInteractor = new EditTaskInteractor(dao, editTaskPresenter);
        EditTaskController editTaskController = new EditTaskController(editTaskInteractor);

        // TODO: After sign-in, use the authenticated user's ID to load their tasks from
        //  the shared DAO. Populate the task list with those results and remove the sample data.
        List<EditTaskOutputData> rows = samples.stream().map(task -> new EditTaskOutputData(
                task.getUniqueTaskId(), task.getUniqueUserId(), task.getTitle(), task.getDescription(),
                task.getDeadline(), task.getEstimatedHoursToCompleteTask(), task.getCompletionProgress(),
                task.getDifficultyLevel())).collect(Collectors.toList());

        // TODO: Register the main view in cardPanel under its own view name and navigate
        //  there after sign-in. Then remove the sample button, this TaskListView registration,
        //  and the SAMPLE_TASKS constant.
        cardPanel.add(new TaskListView(sampleUserId, rows, editTaskController, editTaskViewModel), SAMPLE_TASKS);
    }

    private void show(String name) {
        viewManagerModel.setState(name);
        viewManagerModel.notifyViewChanged();
    }

    public JFrame build() {
        JFrame application = new JFrame("Student Task Planner");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);
        show(WelcomeViewModel.VIEW_NAME);
        return application;
    }
}

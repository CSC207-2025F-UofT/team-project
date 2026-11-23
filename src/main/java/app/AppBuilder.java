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
        JButton openTasks = new JButton("Open sample tasks");
        openTasks.addActionListener(event -> show(SAMPLE_TASKS));
        welcome.add(openTasks);
        cardPanel.add(welcome, WelcomeViewModel.VIEW_NAME);

        // This branch has no login or persistent task list yet.
        int sampleUserId = 1;
        LocalDate today = LocalDate.now();
        List<StudentTask> samples = List.of(
                new StudentTask(1, sampleUserId, "Finish CSC207 MVP", "Review the edit flow",
                        today.plusDays(7), 10, (byte) 20, (byte) 2),
                new StudentTask(2, sampleUserId, "Write unit tests", "", today.plusDays(3),
                        3, (byte) 0, (byte) 1));
        InMemoryTaskDataAccess dao = new InMemoryTaskDataAccess(samples);
        EditTaskViewModel viewModel = new EditTaskViewModel();
        EditTaskPresenter presenter = new EditTaskPresenter(viewModel);
        EditTaskController controller = new EditTaskController(new EditTaskInteractor(dao, presenter));
        List<EditTaskOutputData> rows = samples.stream().map(task -> new EditTaskOutputData(
                task.getUniqueTaskId(), task.getUniqueUserId(), task.getTitle(), task.getDescription(),
                task.getDeadline(), task.getEstimatedHoursToCompleteTask(), task.getCompletionProgress(),
                task.getDifficultyLevel())).collect(Collectors.toList());
        cardPanel.add(new TaskListView(sampleUserId, rows, controller, viewModel), SAMPLE_TASKS);
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

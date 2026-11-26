package use_case.monitoring;

import entity.StudentTask;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MonitoringInteractor implements MonitoringInputBoundary {

    private final MonitoringDataAccessInterface monitoringDataAccessObject;
    private final HolidayService holidayService;
    private final MonitoringOutputBoundary monitoringPresenter;

    public MonitoringInteractor(MonitoringDataAccessInterface monitoringDataAccessObject,
                                HolidayService holidayService, MonitoringOutputBoundary monitoringPresenter) {
        this.monitoringDataAccessObject = monitoringDataAccessObject;
        this.holidayService = holidayService;
        this.monitoringPresenter = monitoringPresenter;
    }

    @Override
    public void execute(MonitoringInputData inputData) {
        int uniqueUserId = inputData.getUniqueUserId();

        List<StudentTask> tasks = monitoringDataAccessObject.getTasksByUserId(uniqueUserId);

        String countryCode = monitoringDataAccessObject.getCountryCodeByUserId(uniqueUserId);

        if (countryCode == null || countryCode.isBlank()) {
            monitoringPresenter.prepareFailView("Country code not found.");
            return;
        }

        LocalDate today = LocalDate.now();

        Comparator<StudentTask> sortByDeadlineThenTitle =
                new Comparator<StudentTask>() {
                    @Override
                    public int compare(StudentTask first, StudentTask second) {
                        int deadlineComparison = first.getDeadline().compareTo(second.getDeadline());

                        if (deadlineComparison != 0) {
                            return deadlineComparison;
                        }

                        return first.getTitle().compareToIgnoreCase(second.getTitle());
                    }
                };

        List<StudentTask> incompleteTasks = new ArrayList<>();

        for (StudentTask task : tasks) {
            if (task.getCompletionProgress() < 100) {
                incompleteTasks.add(task);
            }
        }

        List<StudentTask> overdueTasks = new ArrayList<>();
        List<StudentTask> upcomingTasks = new ArrayList<>();

        for (StudentTask task : incompleteTasks) {
            boolean overdue = !task.getDeadline().isAfter(today);

            if (overdue) {
                overdueTasks.add(task);
            } else {
                upcomingTasks.add(task);
            }
        }

        overdueTasks.sort(sortByDeadlineThenTitle);
        upcomingTasks.sort(sortByDeadlineThenTitle);

        List<StudentTask> sortedTasks = new ArrayList<>();
        sortedTasks.addAll(overdueTasks);
        sortedTasks.addAll(upcomingTasks);

        List<MonitoringOutputData.MonitoringTaskData> outputTasks = new ArrayList<>();

        for (StudentTask task : sortedTasks) {
            boolean overdue = !task.getDeadline().isAfter(today);

            Optional<String> holidayName = holidayService.getHolidayName(task.getDeadline(), countryCode);

            MonitoringOutputData.MonitoringTaskData taskData = new MonitoringOutputData.MonitoringTaskData(
                    task.getUniqueTaskId(), task.getTitle(), task.getDeadline(), task.getCompletionProgress(),
                    task.getDifficultyLevel().toString(), overdue, holidayName);

            outputTasks.add(taskData);
        }

        MonitoringOutputData outputData = new MonitoringOutputData(outputTasks);

        monitoringPresenter.prepareSuccessView(outputData);
    }
}








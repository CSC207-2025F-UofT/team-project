package use_case.monitoring;

import entity.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MonitoringInteractor {

    private final HolidayService holidayService;

    public MonitoringInteractor(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    public List<Task> getTasksForMonitoring(List<Task> tasks, String countryCode) {
        LocalDate today = LocalDate.now();

        Comparator<Task> byDeadlineThenTitle = Comparator.comparing(Task::getDeadline).thenComparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER);

        List<Task> incomplete = tasks.stream().filter(task -> task.getCompletionProgress() < 100).collect(Collectors.toList());

        List<Task> overdue = incomplete.stream().filter(task -> !task.getDeadline().isAfter(today)).sorted(byDeadlineThenTitle).collect(Collectors.toList());

        List<Task> upcoming = incomplete.stream().filter(task -> task.getDeadline().isAfter(today)).sorted(byDeadlineThenTitle).collect(Collectors.toList());

        List<Task> result = new ArrayList<>();
        result.addAll(overdue);
        result.addAll(upcoming);

        return result;
    }

    public Optional<String> getHolidayWarning(Task task, String countryCode) {
        return holidayService.getHolidayName(task.getDeadline(), countryCode);
    }
}

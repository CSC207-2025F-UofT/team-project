package use_case.monitoring;

import entity.StudentTask;

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

    public List<StudentTask> getTasksForMonitoring(List<StudentTask> tasks, String countryCode) {
        LocalDate today = LocalDate.now();

        Comparator<StudentTask> byDeadlineThenTitle = new Comparator<StudentTask>() {

            @Override
            public int compare(StudentTask first, StudentTask second) {
                int deadlineComparison = first.getDeadline().compareTo(second.getDeadline());

                if (deadlineComparison != 0) {
                    return deadlineComparison;
                }

                return first.getTitle().compareToIgnoreCase(second.getTitle());
            }
        };

        List<StudentTask> incomplete = tasks.stream().filter(task -> task.getCompletionProgress() < 100).collect(Collectors.toList());

        List<StudentTask> overdue = incomplete.stream().filter(task -> !task.getDeadline().isAfter(today)).sorted(byDeadlineThenTitle).collect(Collectors.toList());

        List<StudentTask> upcoming = incomplete.stream().filter(task -> task.getDeadline().isAfter(today)).sorted(byDeadlineThenTitle).collect(Collectors.toList());

        List<StudentTask> result = new ArrayList<>();
        result.addAll(overdue);
        result.addAll(upcoming);

        return result;
    }

    public Optional<String> getHolidayWarning(StudentTask task, String countryCode) {
        return holidayService.getHolidayName(task.getDeadline(), countryCode);
    }
}


package use_case.monitoring;

import entity.Task;
import org.junit.jupiter.api.Test;
import entity.StudentTask;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonitoringInteractorTest {

    @Test
    void completedTaskIsNotDisplayed() {
        HolidayService noHolidays = (date, countryCode) -> Optional.empty();
        MonitoringInteractor interactor = new MonitoringInteractor(noHolidays);

        StudentTask completed = new StudentTask(
                1, 1, "Completed assignment", "", LocalDate.now().plusDays(1),
                10.0, (byte) 100, (byte) 2);

        StudentTask incomplete = new StudentTask(
                2, 1, "Incomplete assignment", "", LocalDate.now().plusDays(2),
                10.0, (byte) 0, (byte) 2);

        List<StudentTask> result = interactor.getTasksForMonitoring(
                Arrays.asList(completed, incomplete), "CA");

        assertEquals(1, result.size());
        assertEquals("Incomplete assignment", result.get(0).getTitle());
    }
}
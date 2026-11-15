
package use_case.monitoring;

import entity.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonitoringInteractorTest {

    @Test
    void completedTaskIsNotDisplayed() {
        HolidayService noHolidays = (date, countryCode) -> Optional.empty();
        MonitoringInteractor interactor =
                new MonitoringInteractor(noHolidays);

        Task completed = new Task(
                "Completed assignment",
                LocalDate.now().plusDays(1),
                (byte) 100
        );

        Task incomplete = new Task(
                "Incomplete assignment",
                LocalDate.now().plusDays(2),
                (byte) 0
        );

        List<Task> result = interactor.getTasksForMonitoring(
                Arrays.asList(completed, incomplete),
                "CA"
        );

        assertEquals(1, result.size());
        assertEquals("Incomplete assignment", result.get(0).getTitle());
    }
}
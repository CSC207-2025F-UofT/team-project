package use_case.monitoring;

import org.junit.jupiter.api.Test;
import entity.DifficultyLevel;
import entity.StudentTask;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonitoringInteractorTest {

    @Test
    void completedTaskIsNotDisplayed() {

        StudentTask completed = new StudentTask(1, 1, "Completed assignment", "",
                LocalDate.now().plusDays(1), 10.0, (byte) 100,
                DifficultyLevel.AVERAGE);

        StudentTask incomplete = new StudentTask(2, 1, "Incomplete assignment",
                "", LocalDate.now().plusDays(2), 10.0, (byte) 0,
                DifficultyLevel.AVERAGE);

        MonitoringDataAccessInterface fakeDataAccess = new MonitoringDataAccessInterface() {
                    @Override
                    public List<StudentTask> getTasksByUserId(int uniqueUserId) {
                        return Arrays.asList(completed, incomplete);
                    }

                    @Override
                    public String getCountryCodeByUserId(int uniqueUserId) {
                        return "CA";
                    }
                };

        HolidayService noHolidays = (date, countryCode) -> Optional.empty();

        List<MonitoringOutputData> receivedOutput = new ArrayList<MonitoringOutputData>();

        MonitoringOutputBoundary fakePresenter = new MonitoringOutputBoundary() {
                    @Override
                    public void prepareSuccessView(MonitoringOutputData outputData) {
                        receivedOutput.add(outputData);
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        throw new AssertionError(errorMessage);
                    }
                };

        MonitoringInteractor interactor = new MonitoringInteractor(fakeDataAccess, noHolidays, fakePresenter);

        interactor.execute(new MonitoringInputData(1));

        MonitoringOutputData outputData = receivedOutput.get(0);

        assertEquals(1, outputData.getTasks().size());
        assertEquals("Incomplete assignment", outputData.getTasks().get(0).getTitle());
    }
}
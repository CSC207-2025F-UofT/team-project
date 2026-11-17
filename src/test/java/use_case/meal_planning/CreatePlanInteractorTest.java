package use_case.meal_planning;

import entity.DailyMealPlan;
import entity.MealSchedule;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreatePlanInteractorTest {

    @Test
    void rejectsMissingDate() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();
        CreatePlanInteractor interactor =
                new CreatePlanInteractor(dataAccess, presenter);

        interactor.execute(new CreatePlanInputData(
                null,
                List.of("Breakfast")
        ));

        assertEquals("Please select a date.", presenter.errorMessage);
        assertNull(presenter.outputData);
        assertNull(dataAccess.savedPlan);
        assertNull(dataAccess.savedSchedule);
    }

    @Test
    void reportsStorageReadFailure() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        dataAccess.failOnRead = true;

        FakePresenter presenter = new FakePresenter();
        CreatePlanInteractor interactor =
                new CreatePlanInteractor(dataAccess, presenter);

        interactor.execute(new CreatePlanInputData(
                LocalDate.of(2026, 11, 18),
                List.of("Breakfast")
        ));

        assertEquals(
                "Unable to access meal plan storage. Please try again.",
                presenter.errorMessage
        );
        assertNull(presenter.outputData);
        assertNull(dataAccess.savedPlan);
        assertNull(dataAccess.savedSchedule);
    }

    @Test
    void reportsStorageSaveFailure() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        dataAccess.failOnSave = true;

        FakePresenter presenter = new FakePresenter();
        CreatePlanInteractor interactor =
                new CreatePlanInteractor(dataAccess, presenter);

        interactor.execute(new CreatePlanInputData(
                LocalDate.of(2026, 11, 18),
                List.of("Breakfast")
        ));

        assertEquals(
                "Unable to access meal plan storage. Please try again.",
                presenter.errorMessage
        );
        assertNull(presenter.outputData);
        assertNull(dataAccess.savedPlan);
        assertNull(dataAccess.savedSchedule);
    }

    @Test
    void rejectsBlankMealSlotName() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();
        CreatePlanInteractor interactor =
                new CreatePlanInteractor(dataAccess, presenter);

        interactor.execute(new CreatePlanInputData(
                LocalDate.of(2026, 11, 18),
                List.of("Breakfast", "   ")
        ));

        assertEquals(
                "Meal slot names cannot be empty.",
                presenter.errorMessage
        );
        assertNull(presenter.outputData);
        assertNull(dataAccess.savedPlan);
        assertNull(dataAccess.savedSchedule);
    }

    @Test
    void createsPlanAndSavesSchedule() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        CreatePlanInteractor interactor =
                new CreatePlanInteractor(dataAccess, presenter);

        LocalDate date = LocalDate.of(2026, 11, 18);
        CreatePlanInputData inputData = new CreatePlanInputData(
                date,
                List.of(" Breakfast ", "Lunch", "Dinner")
        );

        interactor.execute(inputData);

        // The plan and its schedule were saved.
        assertNotNull(dataAccess.savedPlan);
        assertNotNull(dataAccess.savedSchedule);
        assertEquals(date, dataAccess.savedPlan.getDate());
        assertTrue(dataAccess.savedPlan.getPlannedMeals().isEmpty());

        // Slot order is preserved and surrounding spaces are removed.
        assertEquals(3, dataAccess.savedSchedule.getSlots().size());
        assertEquals("Breakfast",
                dataAccess.savedSchedule.getSlots().get(0).getName());
        assertEquals("Lunch",
                dataAccess.savedSchedule.getSlots().get(1).getName());
        assertEquals("Dinner",
                dataAccess.savedSchedule.getSlots().get(2).getName());

        // Success refers to the plan that was actually saved.
        assertNotNull(presenter.outputData);
        assertEquals(dataAccess.savedPlan.getId(),
                presenter.outputData.getPlanId());
        assertEquals(date, presenter.outputData.getDate());
        assertNull(presenter.errorMessage);
    }

    @Test
    void rejectsSecondPlanForSameDate() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter firstPresenter = new FakePresenter();
        LocalDate date = LocalDate.of(2026, 11, 18);

        // Create the original plan.
        CreatePlanInteractor firstInteractor =
                new CreatePlanInteractor(dataAccess, firstPresenter);

        firstInteractor.execute(new CreatePlanInputData(
                date,
                List.of("Breakfast", "Lunch")
        ));

        assertNotNull(firstPresenter.outputData);

        DailyMealPlan originalPlan = dataAccess.savedPlan;
        MealSchedule originalSchedule = dataAccess.savedSchedule;

        // Attempt to create another plan for the same date.
        FakePresenter secondPresenter = new FakePresenter();
        CreatePlanInteractor secondInteractor =
                new CreatePlanInteractor(dataAccess, secondPresenter);

        secondInteractor.execute(new CreatePlanInputData(
                date,
                List.of("Dinner")
        ));

        // The second request fails without reporting success.
        assertNull(secondPresenter.outputData);
        assertEquals(
                "A meal plan already exists for this date.",
                secondPresenter.errorMessage
        );

        // The original plan and schedule remain saved.
        assertSame(originalPlan, dataAccess.savedPlan);
        assertSame(originalSchedule, dataAccess.savedSchedule);
        assertEquals(2, dataAccess.savedSchedule.getSlots().size());
    }

    private static class FakeDataAccess
            implements CreatePlanDataAccessInterface {

        private DailyMealPlan savedPlan;
        private MealSchedule savedSchedule;
        private boolean failOnRead;
        private boolean failOnSave;

        @Override
        public boolean existsByDate(LocalDate date)
                throws CreatePlanDataAccessException {
            if (failOnRead) {
                throw new CreatePlanDataAccessException(
                        "Simulated read failure.");
            }

            return savedPlan != null
                    && savedPlan.getDate().equals(date);
        }

        @Override
        public void save(DailyMealPlan plan, MealSchedule schedule)
                throws CreatePlanDataAccessException {
            if (failOnSave) {
                throw new CreatePlanDataAccessException(
                        "Simulated save failure.");
            }

            savedPlan = plan;
            savedSchedule = schedule;
        }
    }

    private static class FakePresenter
            implements CreatePlanOutputBoundary {

        private CreatePlanOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(CreatePlanOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}

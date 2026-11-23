package use_case.remove_planned_meal;

import data_access.InMemoryMealPlanDataAccessObject;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.PlannedMeal;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RemovePlannedMealInteractorTest {

    private static final LocalDate MONDAY = LocalDate.of(2026, 11, 16);
    private static final MealSlot TUESDAY_DINNER = new MealSlot(DayOfWeek.TUESDAY, MealType.DINNER);

    private static class CapturingPresenter implements RemovePlannedMealOutputBoundary {
        RemovePlannedMealOutputData success;
        String error;

        @Override
        public void prepareSuccessView(RemovePlannedMealOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }

    private static InMemoryMealPlanDataAccessObject daoWithPasta() {
        InMemoryMealPlanDataAccessObject dao = new InMemoryMealPlanDataAccessObject();
        MealPlan plan = new MealPlan(MONDAY);
        plan.assign(TUESDAY_DINNER, new PlannedMeal("1", "Pasta", List.of()));
        dao.saveMealPlan(plan);
        return dao;
    }

    @Test
    void removesMealAndSaves() {
        InMemoryMealPlanDataAccessObject dao = daoWithPasta();
        CapturingPresenter presenter = new CapturingPresenter();

        new RemovePlannedMealInteractor(dao, presenter).execute(
                new RemovePlannedMealInputData(MONDAY, DayOfWeek.TUESDAY, MealType.DINNER));

        assertNull(presenter.error);
        assertEquals("Pasta", presenter.success.getRemovedRecipeName());
        assertTrue(presenter.success.getWeek().isEmpty());
        assertTrue(dao.getMealPlan(MONDAY).getMeals().isEmpty());
    }

    @Test
    void cookedMealCanStillBeRemoved() {
        InMemoryMealPlanDataAccessObject dao = daoWithPasta();
        dao.getMealPlan(MONDAY).getMeals().get(TUESDAY_DINNER).markCooked();
        CapturingPresenter presenter = new CapturingPresenter();

        new RemovePlannedMealInteractor(dao, presenter).execute(
                new RemovePlannedMealInputData(MONDAY, DayOfWeek.TUESDAY, MealType.DINNER));

        assertNull(presenter.error);
        assertTrue(dao.getMealPlan(MONDAY).getMeals().isEmpty());
    }

    @Test
    void anyDayOfTheWeekFindsThatWeek() {
        InMemoryMealPlanDataAccessObject dao = daoWithPasta();
        CapturingPresenter presenter = new CapturingPresenter();

        new RemovePlannedMealInteractor(dao, presenter).execute(
                new RemovePlannedMealInputData(MONDAY.plusDays(4), DayOfWeek.TUESDAY, MealType.DINNER));

        assertNull(presenter.error);
        assertTrue(dao.getMealPlan(MONDAY).getMeals().isEmpty());
    }

    @Test
    void failsWhenSlotIsEmpty() {
        InMemoryMealPlanDataAccessObject dao = daoWithPasta();
        CapturingPresenter presenter = new CapturingPresenter();

        new RemovePlannedMealInteractor(dao, presenter).execute(
                new RemovePlannedMealInputData(MONDAY, DayOfWeek.MONDAY, MealType.LUNCH));

        assertEquals(RemovePlannedMealInteractor.NO_MEAL, presenter.error);
        assertNull(presenter.success);
        assertEquals(1, dao.getMealPlan(MONDAY).getMeals().size());
    }

    @Test
    void failsWhenNothingIsSavedForTheWeek() {
        InMemoryMealPlanDataAccessObject dao = new InMemoryMealPlanDataAccessObject();
        CapturingPresenter presenter = new CapturingPresenter();

        new RemovePlannedMealInteractor(dao, presenter).execute(
                new RemovePlannedMealInputData(MONDAY, DayOfWeek.TUESDAY, MealType.DINNER));

        assertEquals(RemovePlannedMealInteractor.NO_MEAL, presenter.error);
        assertNull(dao.getMealPlan(MONDAY));
    }
}

package use_case.load_meal_plan;

import data_access.InMemoryMealPlanDataAccessObject;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.PlannedMeal;
import org.junit.jupiter.api.Test;
import use_case.meal_plan.MealSlotOutputData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadMealPlanInteractorTest {

    private static final LocalDate MONDAY = LocalDate.of(2026, 11, 16);

    private static class CapturingPresenter implements LoadMealPlanOutputBoundary {
        LoadMealPlanOutputData output;

        @Override
        public void prepareSuccessView(LoadMealPlanOutputData outputData) {
            output = outputData;
        }
    }

    @Test
    void loadsSavedPlan() {
        InMemoryMealPlanDataAccessObject dao = new InMemoryMealPlanDataAccessObject();
        MealPlan plan = new MealPlan(MONDAY);
        MealSlot mondayLunch = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH);
        plan.assign(mondayLunch, new PlannedMeal("52771", "Spicy Arrabiata Penne", List.of()));
        dao.saveMealPlan(plan);
        CapturingPresenter presenter = new CapturingPresenter();

        new LoadMealPlanInteractor(dao, presenter).execute(new LoadMealPlanInputData(MONDAY));

        assertEquals(MONDAY, presenter.output.getWeek().getWeekStart());
        assertFalse(presenter.output.isEmpty());
        MealSlotOutputData lunch = presenter.output.getWeek().getSlots().get(1);
        assertEquals(mondayLunch.getDay(), lunch.getDay());
        assertEquals("LUNCH", lunch.getMealType());
        assertEquals("Spicy Arrabiata Penne", lunch.getRecipeName());
    }

    @Test
    void showsEmptyPlanWhenNothingSaved() {
        CapturingPresenter presenter = new CapturingPresenter();

        new LoadMealPlanInteractor(new InMemoryMealPlanDataAccessObject(), presenter)
                .execute(new LoadMealPlanInputData(MONDAY));

        assertNotNull(presenter.output.getWeek());
        assertEquals(MONDAY, presenter.output.getWeek().getWeekStart());
        assertEquals(21, presenter.output.getWeek().getSlots().size());
        assertTrue(presenter.output.isEmpty());
    }

    @Test
    void savedPlanWithNoMealsCountsAsEmpty() {
        InMemoryMealPlanDataAccessObject dao = new InMemoryMealPlanDataAccessObject();
        dao.saveMealPlan(new MealPlan(MONDAY));
        CapturingPresenter presenter = new CapturingPresenter();

        new LoadMealPlanInteractor(dao, presenter).execute(new LoadMealPlanInputData(MONDAY));

        assertTrue(presenter.output.isEmpty());
    }

    @Test
    void anyDayOfTheWeekLoadsThatWeeksMonday() {
        InMemoryMealPlanDataAccessObject dao = new InMemoryMealPlanDataAccessObject();
        MealPlan plan = new MealPlan(MONDAY);
        plan.assign(new MealSlot(DayOfWeek.FRIDAY, MealType.DINNER),
                new PlannedMeal("1", "Pasta", List.of()));
        dao.saveMealPlan(plan);
        CapturingPresenter presenter = new CapturingPresenter();
        LocalDate thursday = MONDAY.plusDays(3);
        LocalDate sunday = MONDAY.plusDays(6);

        LoadMealPlanInteractor interactor = new LoadMealPlanInteractor(dao, presenter);
        interactor.execute(new LoadMealPlanInputData(thursday));
        assertEquals(MONDAY, presenter.output.getWeek().getWeekStart());
        assertFalse(presenter.output.isEmpty());
        interactor.execute(new LoadMealPlanInputData(sunday));
        assertEquals(MONDAY, presenter.output.getWeek().getWeekStart());
        assertFalse(presenter.output.isEmpty());
    }

    @Test
    void doesNotLoadNeighbouringWeek() {
        InMemoryMealPlanDataAccessObject dao = new InMemoryMealPlanDataAccessObject();
        dao.saveMealPlan(new MealPlan(MONDAY));
        CapturingPresenter presenter = new CapturingPresenter();

        new LoadMealPlanInteractor(dao, presenter)
                .execute(new LoadMealPlanInputData(MONDAY.plusDays(7)));

        assertEquals(MONDAY.plusDays(7), presenter.output.getWeek().getWeekStart());
        assertTrue(presenter.output.isEmpty());
    }
}

package use_case.move_planned_meal;

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

class MovePlannedMealInteractorTest {

    private static final LocalDate MONDAY = LocalDate.of(2026, 11, 16);
    private static final MealSlot MONDAY_LUNCH = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH);
    private static final MealSlot FRIDAY_DINNER = new MealSlot(DayOfWeek.FRIDAY, MealType.DINNER);
    private static final MealSlot SUNDAY_BREAKFAST = new MealSlot(DayOfWeek.SUNDAY, MealType.BREAKFAST);

    private static class CapturingPresenter implements MovePlannedMealOutputBoundary {
        MovePlannedMealOutputData success;
        String error;

        @Override
        public void prepareSuccessView(MovePlannedMealOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }

    /** Pasta on Monday lunch, soup on Friday dinner. */
    private static InMemoryMealPlanDataAccessObject daoWithTwoMeals() {
        InMemoryMealPlanDataAccessObject dao = new InMemoryMealPlanDataAccessObject();
        MealPlan plan = new MealPlan(MONDAY);
        plan.assign(MONDAY_LUNCH, new PlannedMeal("1", "Pasta", List.of()));
        plan.assign(FRIDAY_DINNER, new PlannedMeal("2", "Soup", List.of()));
        dao.saveMealPlan(plan);
        return dao;
    }

    private static void move(InMemoryMealPlanDataAccessObject dao, CapturingPresenter presenter,
                             MealSlot from, MealSlot to) {
        new MovePlannedMealInteractor(dao, presenter).execute(new MovePlannedMealInputData(
                MONDAY, from.getDay(), from.getMealType(), to.getDay(), to.getMealType()));
    }

    @Test
    void movesMealToEmptySlotAndSaves() {
        InMemoryMealPlanDataAccessObject dao = daoWithTwoMeals();
        CapturingPresenter presenter = new CapturingPresenter();

        move(dao, presenter, MONDAY_LUNCH, SUNDAY_BREAKFAST);

        assertNull(presenter.error);
        assertEquals("Pasta", presenter.success.getMovedRecipeName());
        MealPlan saved = dao.getMealPlan(MONDAY);
        assertFalse(saved.getMeals().containsKey(MONDAY_LUNCH));
        assertEquals("Pasta", saved.getMeals().get(SUNDAY_BREAKFAST).getRecipeName());
        assertEquals(2, saved.getMeals().size());
    }

    @Test
    void failsWhenTargetSlotIsTaken() {
        InMemoryMealPlanDataAccessObject dao = daoWithTwoMeals();
        CapturingPresenter presenter = new CapturingPresenter();

        move(dao, presenter, MONDAY_LUNCH, FRIDAY_DINNER);

        assertEquals(MovePlannedMealInteractor.SLOT_TAKEN, presenter.error);
        assertNull(presenter.success);
        assertEquals("Pasta", dao.getMealPlan(MONDAY).getMeals().get(MONDAY_LUNCH).getRecipeName());
        assertEquals("Soup", dao.getMealPlan(MONDAY).getMeals().get(FRIDAY_DINNER).getRecipeName());
    }

    @Test
    void failsWhenMealIsAlreadyCooked() {
        InMemoryMealPlanDataAccessObject dao = daoWithTwoMeals();
        dao.getMealPlan(MONDAY).getMeals().get(MONDAY_LUNCH).markCooked();
        CapturingPresenter presenter = new CapturingPresenter();

        move(dao, presenter, MONDAY_LUNCH, SUNDAY_BREAKFAST);

        assertEquals(MovePlannedMealInteractor.ALREADY_COOKED, presenter.error);
        assertTrue(dao.getMealPlan(MONDAY).getMeals().containsKey(MONDAY_LUNCH));
        assertFalse(dao.getMealPlan(MONDAY).getMeals().containsKey(SUNDAY_BREAKFAST));
    }

    @Test
    void failsWhenSourceSlotIsEmpty() {
        InMemoryMealPlanDataAccessObject dao = daoWithTwoMeals();
        CapturingPresenter presenter = new CapturingPresenter();

        move(dao, presenter, SUNDAY_BREAKFAST, new MealSlot(DayOfWeek.SUNDAY, MealType.LUNCH));

        assertEquals(MovePlannedMealInteractor.NO_MEAL, presenter.error);
    }

    @Test
    void failsWhenNothingIsSavedForTheWeek() {
        CapturingPresenter presenter = new CapturingPresenter();

        move(new InMemoryMealPlanDataAccessObject(), presenter, MONDAY_LUNCH, SUNDAY_BREAKFAST);

        assertEquals(MovePlannedMealInteractor.NO_MEAL, presenter.error);
    }

    @Test
    void failsWhenMovingToTheSameSlot() {
        InMemoryMealPlanDataAccessObject dao = daoWithTwoMeals();
        CapturingPresenter presenter = new CapturingPresenter();

        move(dao, presenter, MONDAY_LUNCH, MONDAY_LUNCH);

        assertEquals(MovePlannedMealInteractor.SAME_SLOT, presenter.error);
        assertTrue(dao.getMealPlan(MONDAY).getMeals().containsKey(MONDAY_LUNCH));
    }
}

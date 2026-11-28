package use_case.edit_planned_meal;

import entity.DailyMealPlan;
import entity.FoodItem;
import entity.FoodPortion;
import entity.MealSchedule;
import entity.MealSlot;
import entity.NutritionTotals;
import entity.PlannedMeal;
import org.junit.jupiter.api.Test;

import use_case.meal_planning.AddPlannedMealDataAccessException;
import use_case.meal_planning.AddPlannedMealDataAccessInterface;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChangePlannedPortionInteractorTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 27);
    private static final String SLOT_ID = "breakfast-slot";

    @Test
    void changesGramsAndNutritionButKeepsMealId() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        DailyMealPlan originalPlan = dataAccess.savedPlan;
        FakePresenter presenter = new FakePresenter();

        new ChangePlannedPortionInteractor(dataAccess, presenter)
                .execute(new ChangePlannedPortionInputData(
                        DATE, SLOT_ID, 0, 50));

        PlannedMeal updatedMeal =
                dataAccess.savedPlan.getMealBySlotId(SLOT_ID);

        assertNull(presenter.errorMessage);
        assertNotNull(presenter.outputData);
        assertNotSame(originalPlan, dataAccess.savedPlan);
        assertEquals("meal-1", updatedMeal.getId());
        assertEquals(50,
                updatedMeal.getPortions().get(0).getQuantityGrams());
        assertEquals(100,
                updatedMeal.getTotalNutrition().getCalories());
        assertEquals(5,
                updatedMeal.getTotalNutrition().getProtein());
        assertEquals("meal-1", presenter.outputData.getMealId());

        // Editing the copy did not change the original plan.
        assertEquals(100, originalPlan.getMealBySlotId(SLOT_ID)
                .getPortions().get(0).getQuantityGrams());
    }

    @Test
    void rejectsPortionIndexOutsideTheMeal() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        new ChangePlannedPortionInteractor(dataAccess, presenter)
                .execute(new ChangePlannedPortionInputData(
                        DATE, SLOT_ID, 1, 50));

        assertEquals("The selected food portion does not exist.",
                presenter.errorMessage);
        assertNull(presenter.outputData);
        assertEquals(0, dataAccess.saveCount);
    }

    @Test
    void rejectsInvalidGrams() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        new ChangePlannedPortionInteractor(dataAccess, presenter)
                .execute(new ChangePlannedPortionInputData(
                        DATE, SLOT_ID, 0, 0));

        assertEquals("Food quantity must be greater than zero.",
                presenter.errorMessage);
        assertNull(presenter.outputData);
        assertEquals(0, dataAccess.saveCount);
    }

    @Test
    void failedSaveLeavesOriginalPlanUnchanged() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        DailyMealPlan originalPlan = dataAccess.savedPlan;
        dataAccess.failSave = true;
        FakePresenter presenter = new FakePresenter();

        new ChangePlannedPortionInteractor(dataAccess, presenter)
                .execute(new ChangePlannedPortionInputData(
                        DATE, SLOT_ID, 0, 50));

        assertNull(presenter.outputData);
        assertEquals(
                "Unable to save the planned meal. Please try again.",
                presenter.errorMessage);
        assertSame(originalPlan, dataAccess.savedPlan);
        assertEquals(100, originalPlan.getMealBySlotId(SLOT_ID)
                .getPortions().get(0).getQuantityGrams());
    }

    private static class FakeDataAccess
            implements AddPlannedMealDataAccessInterface {

        private final FoodItem food = new FoodItem(
                "food-1", "Oats",
                new NutritionTotals(200, 10, 30, 5));
        private final MealSchedule schedule =
                new MealSchedule(List.of(
                        new MealSlot(SLOT_ID, "Breakfast")));

        private DailyMealPlan savedPlan =
                new DailyMealPlan("plan-1", DATE);
        private boolean failSave;
        private int saveCount;

        FakeDataAccess() {
            PlannedMeal meal =
                    new PlannedMeal("meal-1", SLOT_ID);
            meal.addPortion(FoodPortion.from(food, 100));
            savedPlan.addMeal(meal);
        }

        @Override
        public DailyMealPlan findPlanByDate(LocalDate date) {
            return DATE.equals(date) ? savedPlan : null;
        }

        @Override
        public MealSchedule findScheduleByPlanId(String planId) {
            return savedPlan.getId().equals(planId)
                    ? schedule : null;
        }

        @Override
        public FoodItem findFoodById(String foodId) {
            return food.getId().equals(foodId) ? food : null;
        }

        @Override
        public List<FoodItem> findAllFoods() {
            return List.of(food);
        }

        @Override
        public void saveUpdatedPlan(DailyMealPlan plan)
                throws AddPlannedMealDataAccessException {
            if (failSave) {
                throw new AddPlannedMealDataAccessException(
                        "Simulated save failure.");
            }
            savedPlan = plan;
            saveCount++;
        }
    }

    private static class FakePresenter
            implements ChangePlannedPortionOutputBoundary {

        private ChangePlannedPortionOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(
                ChangePlannedPortionOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}

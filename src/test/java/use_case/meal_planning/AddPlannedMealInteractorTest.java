package use_case.meal_planning;

import entity.DailyMealPlan;
import entity.FoodItem;
import entity.FoodPortion;
import entity.MealSchedule;
import entity.MealSlot;
import entity.NutritionTotals;
import entity.PlannedMeal;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import use_case.edit_planned_meal.LoadPlannedPortionsOutputData;
import use_case.edit_planned_meal.PlannedPortionOption;

import static org.junit.jupiter.api.Assertions.*;

class AddPlannedMealInteractorTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 24);
    private static final String SLOT_ID = "breakfast-slot";
    private static final String FOOD_ID = "food-1";

    @Test
    void rejectsMissingPlan() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();
        DailyMealPlan originalPlan = dataAccess.savedPlan;

        new AddPlannedMealInteractor(dataAccess, presenter)
                .execute(new AddPlannedMealInputData(
                        DATE.plusDays(1), SLOT_ID, FOOD_ID, 50));

        assertEquals("No meal plan exists for this date.",
                presenter.errorMessage);
        assertNull(presenter.outputData);
        assertSame(originalPlan, dataAccess.savedPlan);
    }

    @Test
    void rejectsMissingMealSlot() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        new AddPlannedMealInteractor(dataAccess, presenter)
                .execute(new AddPlannedMealInputData(
                        DATE, "unknown-slot", FOOD_ID, 50));

        assertEquals("The selected meal slot does not exist.",
                presenter.errorMessage);
        assertNull(presenter.outputData);
        assertTrue(dataAccess.savedPlan.getPlannedMeals().isEmpty());
    }

    @Test
    void rejectsInvalidGrams() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        new AddPlannedMealInteractor(dataAccess, presenter)
                .execute(new AddPlannedMealInputData(
                        DATE, SLOT_ID, FOOD_ID, 0));

        assertEquals("Food quantity must be greater than zero.",
                presenter.errorMessage);
        assertNull(presenter.outputData);
        assertTrue(dataAccess.savedPlan.getPlannedMeals().isEmpty());
    }

    @Test
    void createsMealInEmptySlotAndCalculatesNutrition() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();
        AddPlannedMealInteractor interactor =
                new AddPlannedMealInteractor(dataAccess, presenter);

        interactor.execute(new AddPlannedMealInputData(
                DATE, SLOT_ID, FOOD_ID, 50));

        assertNull(presenter.errorMessage);
        assertNotNull(presenter.outputData);

        PlannedMeal savedMeal =
                dataAccess.savedPlan.getMealBySlotId(SLOT_ID);
        assertNotNull(savedMeal);
        assertEquals(1, savedMeal.getPortions().size());
        assertEquals(50, savedMeal.getPortions()
                .get(0).getQuantityGrams());

        // The food has 200 kcal and 10 g protein per 100 g.
        assertEquals(100, presenter.outputData.getCalories());
        assertEquals(5, presenter.outputData.getProtein());
        assertEquals(savedMeal.getId(),
                presenter.outputData.getMealId());
    }

    @Test
    void appendsFoodToExistingMealAndKeepsMealId() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        PlannedMeal originalMeal =
                new PlannedMeal("meal-1", SLOT_ID);
        originalMeal.addPortion(
                FoodPortion.from(dataAccess.food, 100));
        dataAccess.savedPlan.addMeal(originalMeal);

        FakePresenter presenter = new FakePresenter();
        AddPlannedMealInteractor interactor =
                new AddPlannedMealInteractor(dataAccess, presenter);

        interactor.execute(new AddPlannedMealInputData(
                DATE, SLOT_ID, FOOD_ID, 50));

        PlannedMeal savedMeal =
                dataAccess.savedPlan.getMealBySlotId(SLOT_ID);
        assertNull(presenter.errorMessage);
        assertEquals("meal-1", savedMeal.getId());
        assertEquals(2, savedMeal.getPortions().size());
        assertEquals(300, presenter.outputData.getCalories());
        assertEquals(15, presenter.outputData.getProtein());
    }

    @Test
    void failedSaveDoesNotChangeOriginalPlan() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        PlannedMeal originalMeal =
                new PlannedMeal("meal-1", SLOT_ID);
        originalMeal.addPortion(
                FoodPortion.from(dataAccess.food, 100));
        dataAccess.savedPlan.addMeal(originalMeal);
        DailyMealPlan originalPlan = dataAccess.savedPlan;
        dataAccess.failSave = true;

        FakePresenter presenter = new FakePresenter();
        AddPlannedMealInteractor interactor =
                new AddPlannedMealInteractor(dataAccess, presenter);

        interactor.execute(new AddPlannedMealInputData(
                DATE, SLOT_ID, FOOD_ID, 50));

        assertNull(presenter.outputData);
        assertNotNull(presenter.errorMessage);
        assertSame(originalPlan, dataAccess.savedPlan);
        assertSame(originalMeal,
                dataAccess.savedPlan.getMealBySlotId(SLOT_ID));
        assertEquals(1, originalMeal.getPortions().size());
    }

    @Test
    void loadsExistingPlannedPortion() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        PlannedMeal meal = new PlannedMeal("meal-1", SLOT_ID);
        meal.addPortion(FoodPortion.from(dataAccess.food, 125));
        dataAccess.savedPlan.addMeal(meal);
        FakePresenter presenter = new FakePresenter();

        new AddPlannedMealInteractor(dataAccess, presenter)
                .loadPortions(DATE, SLOT_ID);

        assertNull(presenter.errorMessage);
        assertNotNull(presenter.portionsOutputData);
        assertEquals(1, presenter.portionsOutputData.getPortions().size());

        PlannedPortionOption option =
                presenter.portionsOutputData.getPortions().get(0);
        assertEquals(0, option.getIndex());
        assertEquals("Oats", option.getFoodName());
        assertEquals(125, option.getGrams());
        assertEquals(250, option.getCalories());
    }

    @Test
    void loadsEmptyListForSlotWithoutPlannedMeal() {
        FakeDataAccess dataAccess = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        new AddPlannedMealInteractor(dataAccess, presenter)
                .loadPortions(DATE, SLOT_ID);

        assertNull(presenter.errorMessage);
        assertNotNull(presenter.portionsOutputData);
        assertTrue(presenter.portionsOutputData.getPortions().isEmpty());
    }

    private static class FakeDataAccess
            implements AddPlannedMealDataAccessInterface {

        @Override
        public List<FoodItem> findAllFoods() {
            return List.of(food);
        }

        private DailyMealPlan savedPlan =
                new DailyMealPlan("plan-1", DATE);
        private final MealSchedule schedule =
                new MealSchedule(List.of(
                        new MealSlot(SLOT_ID, "Breakfast")));
        private final FoodItem food = new FoodItem(
                FOOD_ID,
                "Oats",
                new NutritionTotals(200, 10, 30, 5));
        private boolean failSave;

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
            return FOOD_ID.equals(foodId) ? food : null;
        }

        @Override
        public void saveUpdatedPlan(DailyMealPlan plan)
                throws AddPlannedMealDataAccessException {
            if (failSave) {
                throw new AddPlannedMealDataAccessException(
                        "Simulated save failure.");
            }
            savedPlan = plan;
        }
    }

    private static class FakePresenter
            implements AddPlannedMealOutputBoundary {

        private LoadPlannedPortionsOutputData portionsOutputData;

        @Override
        public void preparePortionsView(
                LoadPlannedPortionsOutputData outputData) {
            this.portionsOutputData = outputData;
        }

        @Override
        public void prepareOptionsView(
                AddPlannedMealOptionsOutputData outputData) {
            // These tests only exercise adding a planned meal.
        }

        private AddPlannedMealOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(
                AddPlannedMealOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
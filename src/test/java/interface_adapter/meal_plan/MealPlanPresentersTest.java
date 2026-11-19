package interface_adapter.meal_plan;

import entity.IngredientName;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.PlannedMeal;
import entity.RecipeIngredient;
import interface_adapter.load_meal_plan.LoadMealPlanPresenter;
import interface_adapter.move_planned_meal.MovePlannedMealPresenter;
import interface_adapter.remove_planned_meal.RemovePlannedMealPresenter;
import org.junit.jupiter.api.Test;
import use_case.load_meal_plan.LoadMealPlanOutputData;
import use_case.move_planned_meal.MovePlannedMealOutputData;
import use_case.remove_planned_meal.RemovePlannedMealOutputData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealPlanPresentersTest {

    private static final LocalDate MONDAY = LocalDate.of(2026, 11, 16);

    private static class CountingListener implements MealPlanViewModel.MealPlanViewModelListener {
        int calls;

        @Override
        public void stateChanged() {
            calls++;
        }
    }

    private static MealPlan planWithPasta() {
        MealPlan plan = new MealPlan(MONDAY);
        PlannedMeal pasta = new PlannedMeal("1", "Pasta", List.of(
                new RecipeIngredient(IngredientName.fromRawName("Garlic"), "2 cloves"),
                new RecipeIngredient(IngredientName.fromRawName("Salt"), "")));
        pasta.markCooked();
        plan.assign(new MealSlot(DayOfWeek.WEDNESDAY, MealType.DINNER), pasta);
        return plan;
    }

    private static MealPlanSlotViewData slot(MealPlanState state, DayOfWeek day, String mealType) {
        for (MealPlanSlotViewData slot : state.getSlots()) {
            if (slot.getDay() == day && slot.getMealType().equals(mealType)) {
                return slot;
            }
        }
        return null;
    }

    @Test
    void loadFillsAllCellsInOrderAndNotifies() {
        MealPlanViewModel viewModel = new MealPlanViewModel();
        CountingListener listener = new CountingListener();
        viewModel.addListener(listener);

        new LoadMealPlanPresenter(viewModel)
                .prepareSuccessView(new LoadMealPlanOutputData(planWithPasta(), false));

        MealPlanState state = viewModel.getState();
        assertEquals(1, listener.calls);
        assertEquals(MONDAY, state.getWeekStart());
        assertEquals(21, state.getSlots().size());
        assertEquals(DayOfWeek.MONDAY, state.getSlots().get(0).getDay());
        assertEquals("BREAKFAST", state.getSlots().get(0).getMealType());
        assertEquals(DayOfWeek.SUNDAY, state.getSlots().get(20).getDay());
        assertEquals("DINNER", state.getSlots().get(20).getMealType());
        assertEquals("", state.getMessage());
        assertEquals("", state.getError());

        MealPlanSlotViewData pasta = slot(state, DayOfWeek.WEDNESDAY, "DINNER");
        assertTrue(pasta.isFilled());
        assertTrue(pasta.isCooked());
        assertEquals("Pasta", pasta.getRecipeName());
        assertEquals(2, pasta.getIngredientLines().size());
        assertTrue(pasta.getIngredientLines().get(0).endsWith(": 2 cloves"));
        assertFalse(pasta.getIngredientLines().get(1).contains(":"));

        assertFalse(slot(state, DayOfWeek.MONDAY, "LUNCH").isFilled());
    }

    @Test
    void loadOfEmptyWeekShowsRecipesHint() {
        MealPlanViewModel viewModel = new MealPlanViewModel();

        new LoadMealPlanPresenter(viewModel)
                .prepareSuccessView(new LoadMealPlanOutputData(new MealPlan(MONDAY), true));

        assertEquals(MealPlanPresenterHelper.EMPTY_WEEK_HINT, viewModel.getState().getMessage());
        assertEquals(21, viewModel.getState().getSlots().size());
    }

    @Test
    void removeAndMoveSuccessShowMessageAndClearError() {
        MealPlanViewModel viewModel = new MealPlanViewModel();
        viewModel.getState().setError("old error");

        new RemovePlannedMealPresenter(viewModel)
                .prepareSuccessView(new RemovePlannedMealOutputData(planWithPasta(), "Soup"));
        assertEquals("Removed Soup.", viewModel.getState().getMessage());
        assertEquals("", viewModel.getState().getError());

        new MovePlannedMealPresenter(viewModel)
                .prepareSuccessView(new MovePlannedMealOutputData(planWithPasta(), "Pasta"));
        assertEquals("Moved Pasta.", viewModel.getState().getMessage());
    }

    @Test
    void failViewsShowErrorAndKeepGrid() {
        MealPlanViewModel viewModel = new MealPlanViewModel();
        new LoadMealPlanPresenter(viewModel)
                .prepareSuccessView(new LoadMealPlanOutputData(planWithPasta(), false));
        CountingListener listener = new CountingListener();
        viewModel.addListener(listener);

        new MovePlannedMealPresenter(viewModel).prepareFailView("That slot already has a meal.");
        assertEquals("That slot already has a meal.", viewModel.getState().getError());
        assertEquals("", viewModel.getState().getMessage());
        assertTrue(slot(viewModel.getState(), DayOfWeek.WEDNESDAY, "DINNER").isFilled());

        new RemovePlannedMealPresenter(viewModel).prepareFailView("There is no meal in that slot.");
        assertEquals("There is no meal in that slot.", viewModel.getState().getError());
        assertEquals(2, listener.calls);
    }
}

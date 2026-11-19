package interface_adapter.meal_plan;

import entity.MealPlan;
import entity.MealSlot;
import entity.PlannedMeal;
import entity.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns a MealPlan into the 21 cells the meal plan page shows. Shared by the
 * load, remove and move presenters so the grid always looks the same.
 */
public final class MealPlanPresenterHelper {

    static final String EMPTY_WEEK_HINT =
            "Nothing is planned for this week yet. Go to the Recipes page to add meals.";

    private MealPlanPresenterHelper() {
    }

    /**
     * Fills the state with the given week. If no meal is planned, the message
     * is set to a hint pointing to the Recipes page; otherwise to the given message.
     *
     * @param state the state to update
     * @param mealPlan the week to show
     * @param message the message to show when the week is not empty
     */
    public static void applyPlan(MealPlanState state, MealPlan mealPlan, String message) {
        List<MealPlanSlotViewData> slots = new ArrayList<>();
        for (MealSlot slot : MealSlot.allSlots()) {
            slots.add(toViewData(slot, mealPlan.getMeals().get(slot)));
        }
        state.setWeekStart(mealPlan.getWeekStart());
        state.setSlots(slots);
        state.setMessage(mealPlan.getMeals().isEmpty() ? EMPTY_WEEK_HINT : message);
        state.setError("");
    }

    private static MealPlanSlotViewData toViewData(MealSlot slot, PlannedMeal meal) {
        String mealType = slot.getMealType().name();
        if (meal == null) {
            return new MealPlanSlotViewData(slot.getDay(), mealType, "", false, List.of());
        }
        List<String> lines = new ArrayList<>();
        for (RecipeIngredient ingredient : meal.getIngredients()) {
            String measure = ingredient.getMeasureDescription();
            String name = ingredient.getDisplayName();
            lines.add(measure == null || measure.isBlank() ? name : name + ": " + measure);
        }
        return new MealPlanSlotViewData(
                slot.getDay(), mealType, meal.getRecipeName(), meal.isCooked(), lines);
    }
}

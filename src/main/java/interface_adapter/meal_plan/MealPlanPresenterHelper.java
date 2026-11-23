package interface_adapter.meal_plan;

import use_case.meal_plan.MealPlanWeekOutputData;
import use_case.meal_plan.MealSlotOutputData;
import use_case.meal_plan.PlannedIngredientOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns the week from the output data into the 21 cells the meal plan page shows.
 * Shared by the load, remove and move presenters so the grid always looks the same.
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
     * @param week the week to show
     * @param message the message to show when the week is not empty
     */
    public static void applyWeek(MealPlanState state, MealPlanWeekOutputData week, String message) {
        List<MealPlanSlotViewData> slots = new ArrayList<>();
        for (MealSlotOutputData slot : week.getSlots()) {
            slots.add(toViewData(slot));
        }
        state.setWeekStart(week.getWeekStart());
        state.setSlots(slots);
        state.setMessage(week.isEmpty() ? EMPTY_WEEK_HINT : message);
        state.setError("");
    }

    private static MealPlanSlotViewData toViewData(MealSlotOutputData slot) {
        List<String> lines = new ArrayList<>();
        for (PlannedIngredientOutputData ingredient : slot.getIngredients()) {
            String measure = ingredient.getMeasure();
            String name = ingredient.getName();
            lines.add(measure.isEmpty() ? name : name + ": " + measure);
        }
        return new MealPlanSlotViewData(slot.getDay(), slot.getMealType(),
                slot.getRecipeName(), slot.isCooked(), lines);
    }
}

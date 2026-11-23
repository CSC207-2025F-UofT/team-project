package use_case.meal_plan;

import entity.MealPlan;
import entity.MealSlot;
import entity.PlannedMeal;
import entity.RecipeIngredient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One week of the meal plan as plain data: the Monday it starts on and all 21 cells,
 * Monday breakfast first. The use case 3 interactors (load, remove, move) return this,
 * so the MealPlan entity never leaves the use case layer.
 */
public class MealPlanWeekOutputData {

    private final LocalDate weekStart;
    private final List<MealSlotOutputData> slots;

    /**
     * Creates output data for a week.
     *
     * @param weekStart the Monday that starts the week
     * @param slots all cells of the week, Monday breakfast first
     */
    public MealPlanWeekOutputData(LocalDate weekStart, List<MealSlotOutputData> slots) {
        this.weekStart = weekStart;
        this.slots = new ArrayList<>(slots);
    }

    /**
     * Copies a meal plan into plain data. Only interactors call this.
     *
     * @param plan the meal plan
     * @return the week as plain data
     */
    public static MealPlanWeekOutputData from(MealPlan plan) {
        List<MealSlotOutputData> slots = new ArrayList<>();
        for (MealSlot slot : MealSlot.allSlots()) {
            slots.add(toSlotData(slot, plan.getMeals().get(slot)));
        }
        return new MealPlanWeekOutputData(plan.getWeekStart(), slots);
    }

    private static MealSlotOutputData toSlotData(MealSlot slot, PlannedMeal meal) {
        String mealType = slot.getMealType().name();
        if (meal == null) {
            return new MealSlotOutputData(slot.getDay(), mealType, "", false, List.of());
        }
        List<PlannedIngredientOutputData> ingredients = new ArrayList<>();
        for (RecipeIngredient ingredient : meal.getIngredients()) {
            ingredients.add(new PlannedIngredientOutputData(
                    ingredient.getDisplayName(), ingredient.getMeasureDescription()));
        }
        return new MealSlotOutputData(
                slot.getDay(), mealType, meal.getRecipeName(), meal.isCooked(), ingredients);
    }

    /**
     * Returns the Monday that starts the week.
     *
     * @return the week start
     */
    public LocalDate getWeekStart() {
        return weekStart;
    }

    /**
     * Returns all cells of the week, Monday breakfast first.
     *
     * @return the cells
     */
    public List<MealSlotOutputData> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    /**
     * Returns whether no meal is planned for the week.
     *
     * @return true if every cell is empty
     */
    public boolean isEmpty() {
        for (MealSlotOutputData slot : slots) {
            if (slot.isFilled()) {
                return false;
            }
        }
        return true;
    }
}

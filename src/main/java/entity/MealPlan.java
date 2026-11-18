package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A weekly meal plan keyed by meal slots. The week always starts on Monday.
 */
public class MealPlan {

    private final LocalDate weekStart;
    private final Map<MealSlot, PlannedMeal> meals = new LinkedHashMap<>();

    /**
     * Creates an empty meal plan for the given week.
     *
     * @param weekStart the Monday that starts the week
     */
    public MealPlan(LocalDate weekStart) {
        if (weekStart == null) {
            throw new IllegalArgumentException("Week start cannot be null");
        }
        this.weekStart = weekStart;
    }

    /**
     * Returns the Monday that starts this week.
     *
     * @return the week start
     */
    public LocalDate getWeekStart() {
        return weekStart;
    }

    /**
     * Returns an unmodifiable view of the planned meals.
     *
     * @return the meals map
     */
    public Map<MealSlot, PlannedMeal> getMeals() {
        return Collections.unmodifiableMap(meals);
    }

    /**
     * Assigns a meal to a slot. Fails if the slot is already taken.
     *
     * @param slot the target slot
     * @param meal the meal to assign
     */
    public void assign(MealSlot slot, PlannedMeal meal) {
        if (slot == null || meal == null) {
            throw new IllegalArgumentException("Slot and meal cannot be null");
        }
        if (meals.containsKey(slot)) {
            throw new IllegalStateException("Slot is already taken");
        }
        meals.put(slot, meal);
    }

    /**
     * Removes the meal in the given slot.
     *
     * @param slot the slot to clear
     */
    public void remove(MealSlot slot) {
        meals.remove(slot);
    }

    /**
     * Moves a meal from one slot to another. Fails if the target is taken or the
     * meal is already cooked.
     *
     * @param from the source slot
     * @param to the destination slot
     */
    public void move(MealSlot from, MealSlot to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Slots cannot be null");
        }
        PlannedMeal meal = meals.get(from);
        if (meal == null) {
            throw new IllegalStateException("Source slot is empty");
        }
        if (meal.isCooked()) {
            throw new IllegalStateException("Cooked meals cannot be moved");
        }
        if (meals.containsKey(to)) {
            throw new IllegalStateException("Target slot is already taken");
        }
        meals.remove(from);
        meals.put(to, meal);
    }

    /**
     * Returns every planned meal that has not been cooked yet.
     *
     * @return the uncooked meals
     */
    public List<PlannedMeal> uncookedMeals() {
        List<PlannedMeal> result = new ArrayList<>();
        for (PlannedMeal meal : meals.values()) {
            if (!meal.isCooked()) {
                result.add(meal);
            }
        }
        return result;
    }
}

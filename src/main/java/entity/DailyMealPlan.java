package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a collection of planned meals for a specific date.
 *
 * A DailyMealPlan contains multiple PlannedMeal objects and
 * provides methods to manage them and calculate the total
 * nutritional information for the day.
 *
 * Planned meals are stored separately from recorded meals,
 * which represent what the user actually consumed.
 */
public class DailyMealPlan {

    // Unique identifier of this daily meal plan.
    private final String id;

    // Date associated with this daily meal plan.
    private final LocalDate date;

    // Collection of planned meals for the date.
    private final List<PlannedMeal> plannedMeals;

    /**
     * Constructs an empty daily meal plan for the specified date.
     *
     * @param id unique identifier of the daily plan
     * @param date date associated with the plan
     * @throws IllegalArgumentException if ID or date is invalid
     */
    public DailyMealPlan(String id, LocalDate date) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Daily meal plan ID cannot be empty.");
        }

        if (date == null) {
            throw new IllegalArgumentException(
                    "Daily meal plan date cannot be null.");
        }

        this.id = id;
        this.date = date;
        this.plannedMeals = new ArrayList<>();
    }

    /**
     * Constructs a daily meal plan with existing planned meals.
     *
     * @param id unique identifier of the daily plan
     * @param date date associated with the plan
     * @param initialMeals planned meals to include
     * @throws IllegalArgumentException if the list is null,
     *                                  contains null, or has duplicate IDs
     */
    public DailyMealPlan(String id, LocalDate date,
                         List<PlannedMeal> initialMeals) {
        this(id, date);

        if (initialMeals == null) {
            throw new IllegalArgumentException(
                    "Initial meals cannot be null.");
        }

        // Reuse addMeal to apply the same validation.
        for (PlannedMeal meal : initialMeals) {
            addMeal(meal);
        }
    }

    /**
     * Returns the unique identifier of this daily plan.
     *
     * @return daily plan ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the date associated with this plan.
     *
     * @return date of the plan
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns an unmodifiable list of planned meals.
     *
     * @return planned meals for this date
     */
    public List<PlannedMeal> getPlannedMeals() {
        return Collections.unmodifiableList(plannedMeals);
    }

    /**
     * Finds a planned meal by its unique identifier.
     *
     * @param mealId ID of the planned meal
     * @return matching PlannedMeal, or null if not found
     */
    public PlannedMeal getMealById(String mealId) {
        for (PlannedMeal meal : plannedMeals) {
            if (meal.getId().equals(mealId)) {
                return meal;
            }
        }
        return null;
    }

    /**
     * Finds a planned meal associated with a specific meal slot.
     *
     * @param mealSlotId ID of the meal slot
     * @return matching PlannedMeal, or null if not found
     */
    public PlannedMeal getMealBySlotId(String mealSlotId) {
        for (PlannedMeal meal : plannedMeals) {
            if (meal.getMealSlotId().equals(mealSlotId)) {
                return meal;
            }
        }
        return null;
    }

    /**
     * Adds a planned meal to this daily plan.
     *
     * Each planned meal must have a unique ID, and each meal
     * slot can have at most one planned meal per day.
     *
     * @param meal planned meal to add
     * @throws IllegalArgumentException if meal is null,
     *                                  its ID already exists,
     *                                  or its meal slot is occupied
     */
    public void addMeal(PlannedMeal meal) {

        if (meal == null) {
            throw new IllegalArgumentException(
                    "Planned meal cannot be null.");
        }

        // Reject duplicate planned meal IDs.
        if (getMealById(meal.getId()) != null) {
            throw new IllegalArgumentException(
                    "Planned meal ID already exists.");
        }

        // Each slot can contain only one planned meal per day.
        if (getMealBySlotId(meal.getMealSlotId()) != null) {
            throw new IllegalArgumentException(
                    "This meal slot already has a planned meal.");
        }

        plannedMeals.add(meal);
    }

    /**
     * Removes a planned meal by its ID.
     *
     * @param mealId ID of the meal to remove
     * @return true if removed, false if no matching meal exists
     */
    public boolean removeMeal(String mealId) {
        return plannedMeals.removeIf(
                meal -> meal.getId().equals(mealId)
        );
    }

    /**
     * Replaces an existing planned meal with another planned meal.
     *
     * The replacement must retain the same meal ID and meal slot.
     * This allows a meal's food portions to be updated without
     * changing its identity or schedule position.
     *
     * @param updatedMeal replacement planned meal
     * @return true if replaced, false if no matching meal exists
     * @throws IllegalArgumentException if updatedMeal is null
     *                                  or changes the original slot
     */
    public boolean replaceMeal(PlannedMeal updatedMeal) {

        if (updatedMeal == null) {
            throw new IllegalArgumentException(
                    "Updated meal cannot be null.");
        }

        for (int i = 0; i < plannedMeals.size(); i++) {
            PlannedMeal existing = plannedMeals.get(i);

            if (existing.getId().equals(updatedMeal.getId())) {

                // Preserve the original meal slot association.
                if (!existing.getMealSlotId().equals(
                        updatedMeal.getMealSlotId())) {
                    throw new IllegalArgumentException(
                            "Cannot change the meal slot when replacing a meal.");
                }

                plannedMeals.set(i, updatedMeal);
                return true;
            }
        }

        return false;
    }

    /**
     * Calculates the total nutritional values of all planned meals.
     *
     * @return combined planned nutrition for the day
     */
    public NutritionTotals getTotalNutrition() {

        // Start with zero nutrition.
        NutritionTotals total =
                new NutritionTotals(0, 0, 0, 0);

        // Sum the nutritional values of every planned meal.
        for (PlannedMeal meal : plannedMeals) {
            total = total.add(meal.getTotalNutrition());
        }

        return total;
    }
}

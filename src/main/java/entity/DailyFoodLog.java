package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents all meals actually consumed on a specific date.
 *
 * A DailyFoodLog contains RecordedMeal objects and provides
 * methods to manage them and calculate the total nutritional
 * intake for the day.
 *
 * Unlike DailyMealPlan, multiple recorded meals may belong
 * to the same meal slot.
 */
public class DailyFoodLog {

    // Unique identifier of this daily food log.
    private final String id;

    // Calendar date associated with this food log.
    private final LocalDate date;

    // Collection of meals actually consumed on this date.
    private final List<RecordedMeal> recordedMeals;

    /**
     * Constructs an empty daily food log.
     *
     * @param id unique identifier of the food log
     * @param date calendar date of the food log
     * @throws IllegalArgumentException if the ID or date is invalid
     */
    public DailyFoodLog(String id, LocalDate date) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Daily food log ID cannot be empty.");
        }

        if (date == null) {
            throw new IllegalArgumentException(
                    "Daily food log date cannot be null.");
        }

        this.id = id;
        this.date = date;
        this.recordedMeals = new ArrayList<>();
    }

    /**
     * Constructs a daily food log with existing recorded meals.
     *
     * This constructor is useful when loading saved food
     * history from persistent storage.
     *
     * @param id unique identifier of the food log
     * @param date calendar date of the food log
     * @param initialMeals recorded meals to include
     * @throws IllegalArgumentException if the list is null,
     *                                  contains null meals,
     *                                  duplicate IDs, or meals
     *                                  from a different date
     */
    public DailyFoodLog(String id, LocalDate date,
                        List<RecordedMeal> initialMeals) {
        this(id, date);

        if (initialMeals == null) {
            throw new IllegalArgumentException(
                    "Initial meals cannot be null.");
        }

        // Reuse addMeal to ensure consistent validation.
        for (RecordedMeal meal : initialMeals) {
            addMeal(meal);
        }
    }

    /**
     * Returns the unique identifier of this food log.
     *
     * @return food log ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the date associated with this food log.
     *
     * @return food log date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns an unmodifiable view of the recorded meals.
     *
     * @return unmodifiable list of recorded meals
     */
    public List<RecordedMeal> getRecordedMeals() {
        return Collections.unmodifiableList(recordedMeals);
    }

    /**
     * Finds a recorded meal using its unique identifier.
     *
     * @param mealId ID of the recorded meal
     * @return matching RecordedMeal, or null if not found
     */
    public RecordedMeal getMealById(String mealId) {
        for (RecordedMeal meal : recordedMeals) {
            if (meal.getId().equals(mealId)) {
                return meal;
            }
        }

        return null;
    }

    /**
     * Adds a recorded meal to this daily food log.
     *
     * The recorded meal must belong to the same date as
     * the log and must have a unique ID within the log.
     *
     * Multiple meals may belong to the same meal slot.
     *
     * @param meal recorded meal to add
     * @throws IllegalArgumentException if the meal is null,
     *                                  has a duplicate ID,
     *                                  or belongs to another date
     */
    public void addMeal(RecordedMeal meal) {

        if (meal == null) {
            throw new IllegalArgumentException(
                    "Recorded meal cannot be null.");
        }

        // Reject duplicate recorded meal IDs.
        if (getMealById(meal.getId()) != null) {
            throw new IllegalArgumentException(
                    "Recorded meal ID already exists.");
        }

        // Ensure that the meal belongs to this calendar date.
        LocalDate consumptionDate =
                meal.getConsumedAt().toLocalDate();

        if (!date.equals(consumptionDate)) {
            throw new IllegalArgumentException(
                    "Recorded meal date does not match food log date.");
        }

        recordedMeals.add(meal);
    }

    /**
     * Removes a recorded meal using its unique identifier.
     *
     * @param mealId ID of the meal to remove
     * @return true if removed, false if no matching meal exists
     */
    public boolean removeMeal(String mealId) {
        return recordedMeals.removeIf(
                meal -> meal.getId().equals(mealId)
        );
    }

    /**
     * Calculates the combined nutritional values of all
     * recorded meals in this daily food log.
     *
     * @return total nutritional intake for the date
     */
    public NutritionTotals getTotalNutrition() {

        // Start with zero nutritional values.
        NutritionTotals total =
                new NutritionTotals(0, 0, 0, 0);

        // Add the nutrition of every recorded meal.
        for (RecordedMeal meal : recordedMeals) {
            total = total.add(meal.getTotalNutrition());
        }

        return total;
    }
}

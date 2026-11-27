package entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a meal that the user has actually consumed.
 *
 * RecordedMeal extends the abstract Meal class and inherits
 * its food portion management and nutrition calculation methods.
 *
 * Unlike PlannedMeal, a RecordedMeal stores the date and time
 * when the meal was consumed.
 *
 * Recorded meals are independent of planned meals, ensuring
 * that modifications to a meal plan do not affect meal history.
 */
public class RecordedMeal extends Meal {

    // Date and time when the meal was consumed.
    private final LocalDateTime consumedAt;

    /**
     * Constructs an empty recorded meal.
     *
     * @param id unique identifier of the recorded meal
     * @param mealSlotId identifier of the associated meal slot
     * @param consumedAt date and time when the meal was consumed
     * @throws IllegalArgumentException if any argument is invalid
     */
    public RecordedMeal(String id, String mealSlotId,
                        LocalDateTime consumedAt) {

        // Initialize the common meal attributes using the parent.
        super(id, mealSlotId);

        if (consumedAt == null) {
            throw new IllegalArgumentException(
                    "Consumption date and time cannot be null.");
        }

        this.consumedAt = consumedAt;
    }

    /**
     * Constructs a recorded meal with existing food portions.
     *
     * This constructor is useful when loading saved records
     * or recording food portions from an existing meal plan.
     *
     * @param id unique identifier of the recorded meal
     * @param mealSlotId identifier of the associated meal slot
     * @param consumedAt date and time when the meal was consumed
     * @param initialPortions food portions that were consumed
     * @throws IllegalArgumentException if any argument is invalid
     */
    public RecordedMeal(String id, String mealSlotId,
                        LocalDateTime consumedAt,
                        List<FoodPortion> initialPortions) {

        // Initialize the common attributes and food portions.
        super(id, mealSlotId, initialPortions);

        if (consumedAt == null) {
            throw new IllegalArgumentException(
                    "Consumption date and time cannot be null.");
        }

        this.consumedAt = consumedAt;
    }

    /**
     * Returns the date and time when this meal was consumed.
     *
     * @return consumption timestamp
     */
    public LocalDateTime getConsumedAt() {
        return consumedAt;
    }
}

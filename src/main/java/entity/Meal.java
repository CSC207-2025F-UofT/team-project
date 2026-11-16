package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a meal containing a collection of food portions.
 *
 * This abstract class provides the common attributes and
 * behaviors shared by planned and recorded meals.
 *
 * A Meal cannot be instantiated directly. Instead, it must
 * be represented by a concrete subclass, such as
 * PlannedMeal or RecordedMeal.
 */
public abstract class Meal {

    // Unique identifier of this meal.
    private final String id;

    // ID of the meal slot associated with this meal.
    private final String mealSlotId;

    // Collection of food portions included in the meal.
    private final List<FoodPortion> portions;

    /**
     * Constructs an empty meal with the specified identifiers.
     *
     * @param id unique identifier of the meal
     * @param mealSlotId identifier of the associated meal slot
     * @throws IllegalArgumentException if either ID is null or blank
     */
    protected Meal(String id, String mealSlotId) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Meal ID cannot be empty.");
        }

        if (mealSlotId == null || mealSlotId.isBlank()) {
            throw new IllegalArgumentException(
                    "Meal slot ID cannot be empty.");
        }

        this.id = id;
        this.mealSlotId = mealSlotId;
        this.portions = new ArrayList<>();
    }

    /**
     * Constructs a meal with an existing collection of portions.
     *
     * @param id unique identifier of the meal
     * @param mealSlotId identifier of the associated meal slot
     * @param initialPortions food portions to include
     * @throws IllegalArgumentException if the list is null
     *                                  or contains null elements
     */
    protected Meal(String id, String mealSlotId,
                   List<FoodPortion> initialPortions) {
        this(id, mealSlotId);

        if (initialPortions == null) {
            throw new IllegalArgumentException(
                    "Initial portions cannot be null.");
        }

        // Reuse addPortion to validate each portion.
        for (FoodPortion portion : initialPortions) {
            addPortion(portion);
        }
    }

    /**
     * Returns the unique identifier of this meal.
     *
     * @return meal ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the ID of the associated meal slot.
     *
     * @return meal slot ID
     */
    public String getMealSlotId() {
        return mealSlotId;
    }

    /**
     * Returns an unmodifiable view of the meal's food portions.
     *
     * @return unmodifiable list of food portions
     */
    public List<FoodPortion> getPortions() {
        return Collections.unmodifiableList(portions);
    }

    /**
     * Adds a food portion to this meal.
     *
     * @param portion food portion to add
     * @throws IllegalArgumentException if portion is null
     */
    public void addPortion(FoodPortion portion) {
        if (portion == null) {
            throw new IllegalArgumentException(
                    "Food portion cannot be null.");
        }

        portions.add(portion);
    }

    /**
     * Removes a food portion at the specified index.
     *
     * @param index index of the portion to remove
     * @return removed food portion
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public FoodPortion removePortion(int index) {
        return portions.remove(index);
    }

    /**
     * Replaces a food portion at the specified index.
     *
     * @param index index of the portion to replace
     * @param newPortion replacement food portion
     * @throws IllegalArgumentException if newPortion is null
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public void replacePortion(int index, FoodPortion newPortion) {
        if (newPortion == null) {
            throw new IllegalArgumentException(
                    "Replacement portion cannot be null.");
        }

        portions.set(index, newPortion);
    }

    /**
     * Calculates the combined nutritional values of all
     * food portions contained in this meal.
     *
     * @return total nutritional values of the meal
     */
    public NutritionTotals getTotalNutrition() {

        // Start with zero nutritional values.
        NutritionTotals total =
                new NutritionTotals(0, 0, 0, 0);

        // Add the nutrition of every food portion.
        for (FoodPortion portion : portions) {
            total = total.add(portion.getNutrition());
        }

        return total;
    }
}

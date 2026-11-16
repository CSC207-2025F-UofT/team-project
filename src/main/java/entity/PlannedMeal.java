package entity;

import java.util.List;

/**
 * Represents a meal that the user plans to consume.
 *
 * PlannedMeal extends the abstract Meal class and inherits
 * its food portion management and nutrition calculation methods.
 *
 * The date of the planned meal is managed by DailyMealPlan.
 * Planned meals remain separate from actual recorded meals.
 */
public class PlannedMeal extends Meal {

    /**
     * Constructs an empty planned meal.
     *
     * @param id unique identifier of the planned meal
     * @param mealSlotId identifier of the associated meal slot
     * @throws IllegalArgumentException if either ID is invalid
     */
    public PlannedMeal(String id, String mealSlotId) {

        // Initialize the shared meal attributes using the parent.
        super(id, mealSlotId);
    }

    /**
     * Constructs a planned meal with existing food portions.
     *
     * This constructor is useful when loading a previously
     * saved meal plan or creating a plan from a template.
     *
     * @param id unique identifier of the planned meal
     * @param mealSlotId identifier of the associated meal slot
     * @param initialPortions food portions to include
     * @throws IllegalArgumentException if any argument is invalid
     */
    public PlannedMeal(String id, String mealSlotId,
                       List<FoodPortion> initialPortions) {

        // Delegate initialization and validation to the parent.
        super(id, mealSlotId, initialPortions);
    }
}

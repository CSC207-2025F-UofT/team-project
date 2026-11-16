package entity;

/**
 * Represents a specific quantity of a food item used in a meal.
 *
 * A FoodPortion stores the food's ID, name, serving quantity,
 * and calculated nutritional information.
 *
 * The nutritional values are stored as a snapshot so that
 * changes to the original FoodItem do not affect previously
 * created portions or recorded meals.
 *
 * This class is immutable.
 */
public class FoodPortion {

    // ID of the original food item in the food bank.
    private final String foodId;

    // Name of the food when this portion was created.
    private final String foodName;

    // Quantity of the food in grams.
    private final double quantityGrams;

    // Nutritional information for this specific quantity.
    private final NutritionTotals nutrition;

    /**
     * Constructs a FoodPortion with the specified values.
     *
     * @param foodId unique identifier of the original food
     * @param foodName name of the original food
     * @param quantityGrams quantity of food in grams
     * @param nutrition nutritional values for the given quantity
     * @throws IllegalArgumentException if any argument is invalid
     */
    public FoodPortion(String foodId, String foodName,
                       double quantityGrams, NutritionTotals nutrition) {

        if (foodId == null || foodId.isBlank()) {
            throw new IllegalArgumentException(
                    "Food ID cannot be empty.");
        }

        if (foodName == null || foodName.isBlank()) {
            throw new IllegalArgumentException(
                    "Food name cannot be empty.");
        }

        // A food portion must have a positive, finite quantity.
        if (!Double.isFinite(quantityGrams) || quantityGrams <= 0) {
            throw new IllegalArgumentException(
                    "Food quantity must be positive and finite.");
        }

        if (nutrition == null) {
            throw new IllegalArgumentException(
                    "Nutrition information cannot be null.");
        }

        this.foodId = foodId;
        this.foodName = foodName;
        this.quantityGrams = quantityGrams;
        this.nutrition = nutrition;
    }

    /**
     * Creates a FoodPortion from an existing FoodItem.
     *
     * The nutritional values are calculated based on
     * the specified quantity and stored as a snapshot.
     *
     * @param food food item selected from the food bank
     * @param grams quantity of food in grams
     * @return a new FoodPortion containing the calculated nutrition
     * @throws IllegalArgumentException if food is null or grams is invalid
     */
    public static FoodPortion from(FoodItem food, double grams) {

        if (food == null) {
            throw new IllegalArgumentException(
                    "Food item cannot be null.");
        }

        // Calculate nutrition based on the selected food quantity.
        NutritionTotals calculatedNutrition =
                food.calculateNutrition(grams);

        return new FoodPortion(
                food.getId(),
                food.getName(),
                grams,
                calculatedNutrition
        );
    }

    /**
     * Returns the ID of the original food item.
     *
     * @return food ID
     */
    public String getFoodId() {
        return foodId;
    }

    /**
     * Returns the food name stored in this portion.
     *
     * @return food name
     */
    public String getFoodName() {
        return foodName;
    }

    /**
     * Returns the quantity of food in grams.
     *
     * @return quantity in grams
     */
    public double getQuantityGrams() {
        return quantityGrams;
    }

    /**
     * Returns the nutritional values for this portion.
     *
     * @return calculated nutritional information
     */
    public NutritionTotals getNutrition() {
        return nutrition;
    }
}

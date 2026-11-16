package entity;

/**
 * Represents a food item stored in the application's food bank.
 *
 * Each food item has a unique ID, a name, and nutritional information
 * per 100 grams. This class also provides functionality to calculate
 * nutritional values for a specified quantity of the food.
 *
 * FoodItem is immutable. Editing a food item requires creating a
 * new FoodItem instance with the updated information.
 */
public class FoodItem {

    // Unique identifier used to distinguish food items in the food bank.
    private final String id;

    // Display name of the food item.
    private final String name;

    // Nutritional information standardized to a 100-gram serving.
    private final NutritionTotals nutritionPer100g;

    /**
     * Constructs a FoodItem with the specified information.
     *
     * @param id unique identifier of the food item
     * @param name display name of the food item
     * @param nutritionPer100g nutritional values per 100 grams
     * @throws IllegalArgumentException if any argument is invalid
     */
    public FoodItem(String id, String name,
                    NutritionTotals nutritionPer100g) {

        // Validate the food ID and name before creating the object.
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Food ID cannot be empty.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Food name cannot be empty.");
        }

        // Every food must have nutritional information.
        if (nutritionPer100g == null) {
            throw new IllegalArgumentException(
                    "Nutrition information cannot be null.");
        }

        this.id = id;
        this.name = name;
        this.nutritionPer100g = nutritionPer100g;
    }

    /**
     * Returns the unique identifier of this food item.
     *
     * @return food ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of this food item.
     *
     * @return food name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the nutritional information per 100 grams.
     *
     * @return nutrition per 100 grams
     */
    public NutritionTotals getNutritionPer100g() {
        return nutritionPer100g;
    }

    /**
     * Calculates the nutritional values for a given quantity of this food.
     *
     * The food's nutritional information is stored per 100 grams,
     * so the values are scaled by grams / 100.
     *
     * @param grams quantity of food in grams
     * @return nutritional values for the specified quantity
     * @throws IllegalArgumentException if grams is not positive or finite
     */
    public NutritionTotals calculateNutrition(double grams) {

        // Reject negative, zero, infinite, or NaN quantities.
        if (!Double.isFinite(grams) || grams <= 0) {
            throw new IllegalArgumentException(
                    "Food quantity must be positive and finite.");
        }

        // Example: 250g requires multiplying per-100g nutrition by 2.5.
        return nutritionPer100g.scale(grams / 100.0);
    }
}
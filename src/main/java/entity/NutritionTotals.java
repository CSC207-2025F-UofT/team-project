package entity;

/**
 * Represents nutritional information consisting of calories,
 * protein, carbohydrates, and fat.
 *
 * This class is used to store and calculate nutritional values
 * for individual foods, food portions, meals, and daily totals.
 *
 * NutritionTotals is immutable. Operations such as adding or
 * scaling nutritional values return new NutritionTotals objects
 * instead of modifying the existing object.
 */
public class NutritionTotals {

    // Energy measured in kilocalories (kcal).
    private final double calories;

    // Protein content measured in grams.
    private final double protein;

    // Carbohydrate content measured in grams.
    private final double carbs;

    // Fat content measured in grams.
    private final double fat;

    /**
     * Constructs a NutritionTotals object with the specified values.
     *
     * All nutritional values must be finite and non-negative.
     *
     * @param calories energy in kilocalories
     * @param protein protein content in grams
     * @param carbs carbohydrate content in grams
     * @param fat fat content in grams
     * @throws IllegalArgumentException if any value is negative,
     *                                  NaN, or infinite
     */
    public NutritionTotals(double calories, double protein,
                           double carbs, double fat) {

        // Ensure all nutritional values are valid before storing them.
        if (!Double.isFinite(calories)
                || !Double.isFinite(protein)
                || !Double.isFinite(carbs)
                || !Double.isFinite(fat)
                || calories < 0
                || protein < 0
                || carbs < 0
                || fat < 0) {

            throw new IllegalArgumentException(
                    "Nutrition values must be finite and non-negative.");
        }

        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    /**
     * Returns the calorie value.
     *
     * @return energy in kilocalories
     */
    public double getCalories() {
        return calories;
    }

    /**
     * Returns the protein value.
     *
     * @return protein in grams
     */
    public double getProtein() {
        return protein;
    }

    /**
     * Returns the carbohydrate value.
     *
     * @return carbohydrates in grams
     */
    public double getCarbs() {
        return carbs;
    }

    /**
     * Returns the fat value.
     *
     * @return fat in grams
     */
    public double getFat() {
        return fat;
    }

    /**
     * Combines this object's nutritional values with another
     * NutritionTotals object.
     *
     * This method is useful for calculating total nutrition
     * across multiple foods or meals.
     *
     * @param other nutritional values to add
     * @return a new NutritionTotals containing the combined values
     */
    public NutritionTotals add(NutritionTotals other) {

        // Add each nutrient independently and return a new object.
        return new NutritionTotals(
                calories + other.calories,
                protein + other.protein,
                carbs + other.carbs,
                fat + other.fat
        );
    }

    /**
     * Multiplies all nutritional values by a specified factor.
     *
     * This method is useful for adjusting nutritional information
     * based on the quantity of food consumed.
     *
     * For example, scaling by 2.5 converts nutrition per 100g
     * into nutrition for a 250g serving.
     *
     * @param factor multiplier applied to each nutritional value
     * @return a new NutritionTotals containing the scaled values
     * @throws IllegalArgumentException if factor is negative,
     *                                  NaN, or infinite
     */
    public NutritionTotals scale(double factor) {

        // Reject invalid multipliers before performing calculations.
        if (!Double.isFinite(factor) || factor < 0) {
            throw new IllegalArgumentException(
                    "Scale factor must be finite and non-negative.");
        }

        // Scale all nutrients proportionally without modifying this object.
        return new NutritionTotals(
                calories * factor,
                protein * factor,
                carbs * factor,
                fat * factor
        );
    }
}

package use_case.add_food;

/**
 * Contains the information required to add a new food
 * to the user's food database.
 *
 * This class transfers data from the controller to the
 * Add Food interactor without depending on UI components.
 *
 * Nutritional values are specified per 100 grams.
 */
public class AddFoodInputData {

    // Name of the food entered by the user.
    private final String name;

    // Nutritional information per 100 grams.
    private final double calories;
    private final double protein;
    private final double carbs;
    private final double fat;

    /**
     * Constructs the input data for adding a new food.
     *
     * @param name name of the food
     * @param calories calories per 100 grams
     * @param protein protein in grams per 100 grams
     * @param carbs carbohydrates in grams per 100 grams
     * @param fat fat in grams per 100 grams
     */
    public AddFoodInputData(String name, double calories,
                            double protein, double carbs, double fat) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    /**
     * Returns the food name.
     *
     * @return name of the food
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the calories per 100 grams.
     *
     * @return calories
     */
    public double getCalories() {
        return calories;
    }

    /**
     * Returns the protein per 100 grams.
     *
     * @return protein in grams
     */
    public double getProtein() {
        return protein;
    }

    /**
     * Returns the carbohydrates per 100 grams.
     *
     * @return carbohydrates in grams
     */
    public double getCarbs() {
        return carbs;
    }

    /**
     * Returns the fat per 100 grams.
     *
     * @return fat in grams
     */
    public double getFat() {
        return fat;
    }
}

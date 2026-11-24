package use_case.add_food;

/**
 * Contains the result of successfully adding a new food.
 *
 * This class transfers the newly created food's information
 * from the interactor to the presenter.
 *
 * All nutritional values are specified per 100 grams.
 */
public class AddFoodOutputData {

    // Unique identifier of the newly created food.
    private final String foodId;

    // Name of the newly created food.
    private final String foodName;

    // Nutritional information per 100 grams.
    private final double calories;
    private final double protein;
    private final double carbs;
    private final double fat;

    /**
     * Constructs the output data for a successful Add Food operation.
     *
     * @param foodId unique identifier of the created food
     * @param foodName name of the created food
     * @param calories calories per 100 grams
     * @param protein protein in grams per 100 grams
     * @param carbs carbohydrates in grams per 100 grams
     * @param fat fat in grams per 100 grams
     */
    public AddFoodOutputData(String foodId, String foodName,
                             double calories, double protein,
                             double carbs, double fat) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    /**
     * Returns the unique identifier of the created food.
     *
     * @return food ID
     */
    public String getFoodId() {
        return foodId;
    }

    /**
     * Returns the name of the created food.
     *
     * @return food name
     */
    public String getFoodName() {
        return foodName;
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

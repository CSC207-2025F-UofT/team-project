
package interface_adapter.add_food;

import use_case.add_food.AddFoodInputBoundary;
import use_case.add_food.AddFoodInputData;

/**
 * Controller for the Add Food use case.
 *
 * Receives user input, creates input data, and delegates
 * execution to the Add Food input boundary.
 */
public class AddFoodController {

    private final AddFoodInputBoundary addFoodUseCase;

    /**
     * Constructs the Add Food controller.
     *
     * @param addFoodUseCase input boundary for the use case
     */
    public AddFoodController(AddFoodInputBoundary addFoodUseCase) {
        if (addFoodUseCase == null) {
            throw new IllegalArgumentException(
                    "Add Food input boundary cannot be null.");
        }

        this.addFoodUseCase = addFoodUseCase;
    }

    /**
     * Executes the Add Food use case.
     *
     * @param name food name
     * @param calories calories per 100 grams
     * @param protein protein per 100 grams
     * @param carbs carbohydrates per 100 grams
     * @param fat fat per 100 grams
     */
    public void execute(String name, double calories,
                        double protein, double carbs, double fat) {

        AddFoodInputData inputData = new AddFoodInputData(
                name,
                calories,
                protein,
                carbs,
                fat
        );

        addFoodUseCase.execute(inputData);
    }
}

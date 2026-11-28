package use_case.add_food;

import entity.FoodItem;
import entity.NutritionTotals;

import java.util.UUID;

/**
 * Implements the business logic for the Add Food use case.
 *
 * This interactor validates the user's input, prevents duplicate
 * food names, generates a unique food ID, and saves the new food
 * through a data access interface.
 *
 * The result is communicated through the output boundary,
 * keeping this class independent of the UI and persistence layer.
 */
public class AddFoodInteractor implements AddFoodInputBoundary {

    // Interface used to access the food database.
    private final AddFoodDataAccessInterface foodDataAccess;

    // Interface used to report the outcome to the presenter.
    private final AddFoodOutputBoundary outputBoundary;

    /**
     * Constructs the Add Food interactor.
     *
     * @param foodDataAccess interface for storing and finding foods
     * @param outputBoundary interface for reporting success or failure
     */
    public AddFoodInteractor(
            AddFoodDataAccessInterface foodDataAccess,
            AddFoodOutputBoundary outputBoundary) {

        if (foodDataAccess == null || outputBoundary == null) {
            throw new IllegalArgumentException(
                    "Interactor dependencies cannot be null.");
        }

        this.foodDataAccess = foodDataAccess;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Executes the Add Food use case.
     *
     * @param inputData food name and nutritional information
     */
    @Override
    public void execute(AddFoodInputData inputData) {

        if (inputData == null) {
            outputBoundary.prepareFailView(
                    "Food information cannot be null.");
            return;
        }

        try {
            // Remove unnecessary whitespace around the food name.
            String name = inputData.getName();

            if (name == null || name.isBlank()) {
                outputBoundary.prepareFailView(
                        "Food name cannot be empty.");
                return;
            }

            name = name.trim();

            // Validate nutrition using the existing entity.
            NutritionTotals nutrition = new NutritionTotals(
                    inputData.getCalories(),
                    inputData.getProtein(),
                    inputData.getCarbs(),
                    inputData.getFat()
            );

            // Prevent duplicate food names.
            // The repository compares names case-insensitively.
            if (foodDataAccess.existsByName(name)) {
                outputBoundary.prepareFailView(
                        "A food with this name already exists.");
                return;
            }

            // Generate a unique ID for the new food.
            String foodId = UUID.randomUUID().toString();

            // Regenerate if an ID collision occurs.
            while (foodDataAccess.existsById(foodId)) {
                foodId = UUID.randomUUID().toString();
            }

            // Construct the food entity.
            FoodItem food = new FoodItem(
                    foodId,
                    name,
                    nutrition
            );

            // Save the new food through the repository interface.
            foodDataAccess.addFood(food);

            // Prepare the success result.
            AddFoodOutputData outputData = new AddFoodOutputData(
                    food.getId(),
                    food.getName(),
                    nutrition.getCalories(),
                    nutrition.getProtein(),
                    nutrition.getCarbs(),
                    nutrition.getFat()
            );

            // Notify the presenter.
            outputBoundary.prepareSuccessView(outputData);

        } catch (IllegalArgumentException exception) {
            // Handle invalid input and repository uniqueness errors.
            outputBoundary.prepareFailView(
                    exception.getMessage());
        }
    }
}

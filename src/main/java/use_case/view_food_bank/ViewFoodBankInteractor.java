package use_case.view_food_bank;

import entity.FoodItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Interactor for the View Food Bank use case.
 *
 * Retrieves all foods from the food repository, sorts them
 * alphabetically by name, and passes the results to the
 * output boundary for presentation.
 *
 * The interactor depends on abstractions rather than
 * concrete data access or presentation implementations.
 */
public class ViewFoodBankInteractor
        implements ViewFoodBankInputBoundary {

    private final ViewFoodBankDataAccessInterface foodDataAccess;
    private final ViewFoodBankOutputBoundary outputBoundary;

    /**
     * Constructs the View Food Bank interactor.
     *
     * @param foodDataAccess repository used to retrieve foods
     * @param outputBoundary boundary used to present the result
     * @throws IllegalArgumentException if a dependency is null
     */
    public ViewFoodBankInteractor(
            ViewFoodBankDataAccessInterface foodDataAccess,
            ViewFoodBankOutputBoundary outputBoundary) {

        if (foodDataAccess == null || outputBoundary == null) {
            throw new IllegalArgumentException(
                    "View Food Bank dependencies cannot be null.");
        }

        this.foodDataAccess = foodDataAccess;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Executes the View Food Bank use case.
     *
     * Retrieves all foods, sorts them alphabetically by name
     * without modifying the repository's collection, and
     * forwards the result to the output boundary.
     *
     * An empty repository is treated as a successful result.
     */
    @Override
    public void execute() {

        try {
            // Retrieve the foods from the repository.
            List<FoodItem> foods = foodDataAccess.getAllFoods();

            if (foods == null) {
                throw new IllegalStateException(
                        "Food repository returned a null list.");
            }

            // Create a copy to avoid changing the DAO's list.
            List<FoodItem> sortedFoods = new ArrayList<>(foods);

            // Sort by name, ignoring capitalization.
            sortedFoods.sort(
                    Comparator.comparing(
                            FoodItem::getName,
                            String.CASE_INSENSITIVE_ORDER
                    ).thenComparing(FoodItem::getName)
            );

            // Package the result for the presentation layer.
            ViewFoodBankOutputData outputData =
                    new ViewFoodBankOutputData(sortedFoods);

            outputBoundary.prepareSuccessView(outputData);

        } catch (RuntimeException exception) {
            outputBoundary.prepareFailView(
                    "Unable to retrieve the food bank.");
        }
    }
}


package use_case.view_food_bank;

import entity.FoodItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Output data for the View Food Bank use case.
 *
 * Contains the food items retrieved from the food repository.
 * The interactor constructs this object and passes it to the
 * output boundary for presentation.
 *
 * This class does not contain any Swing-specific information
 * or presentation logic.
 */
public class ViewFoodBankOutputData {

    // The foods retrieved from the repository.
    private final List<FoodItem> foods;

    /**
     * Constructs the output data for the View Food Bank use case.
     *
     * Creates a defensive copy of the supplied list to prevent
     * external modifications from affecting the output data.
     *
     * @param foods the retrieved food items
     * @throws IllegalArgumentException if foods is null
     */
    public ViewFoodBankOutputData(List<FoodItem> foods) {

        if (foods == null) {
            throw new IllegalArgumentException(
                    "Food list cannot be null.");
        }

        this.foods = new ArrayList<>(foods);
    }

    /**
     * Returns an unmodifiable list of retrieved foods.
     *
     * @return list of food items
     */
    public List<FoodItem> getFoods() {
        return Collections.unmodifiableList(foods);
    }
}

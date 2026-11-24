package use_case.view_food_bank;

import entity.FoodItem;

import java.util.List;

/**
 * Data access interface for the View Food Bank use case.
 *
 * Defines the operations required to retrieve foods from
 * the food repository.
 *
 * Implementations may use in-memory storage, JSON files,
 * or another persistence mechanism.
 */
public interface ViewFoodBankDataAccessInterface {

    /**
     * Retrieves all food items stored in the food bank.
     *
     * @return a list of all stored food items;
     *         an empty list if the food bank is empty
     */
    List<FoodItem> getAllFoods();
}

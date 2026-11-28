package use_case.add_food;

import entity.FoodItem;

/**
 * Defines the data access operations required by the Add Food use case.
 *
 * The interactor depends on this interface rather than a concrete
 * storage implementation, following the Dependency Inversion Principle.
 *
 * Implementations must prevent duplicate food IDs and names.
 */
public interface AddFoodDataAccessInterface {

    /**
     * Checks whether a food with the specified ID already exists.
     *
     * @param foodId unique identifier of the food
     * @return true if the ID already exists, false otherwise
     */
    boolean existsById(String foodId);

    /**
     * Checks whether a food with the specified name already exists.
     *
     * Food names must be compared case-insensitively and must
     * ignore leading and trailing whitespace.
     *
     * For example, "Cooked Rice", "cooked rice", and
     * "  Cooked Rice  " are considered duplicates.
     *
     * @param name name of the food to check
     * @return true if an equivalent food name already exists
     */
    boolean existsByName(String name);

    /**
     * Saves a newly created food item.
     *
     * The implementation must reject duplicate IDs or names,
     * rather than overwrite an existing food item.
     *
     * @param food food item to save
     * @throws IllegalArgumentException if the food's ID
     *                                  or name already exists
     */
    void addFood(FoodItem food);
}

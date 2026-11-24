
package data_access;

import entity.FoodItem;
import use_case.add_food.AddFoodDataAccessInterface;
import use_case.view_food_bank.ViewFoodBankDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * In-memory implementation of food data access operations.
 *
 * Supports both the Add Food and View Food Bank use cases.
 *
 * Food items are stored in a HashMap using their unique IDs.
 * Food names must also be unique, ignoring capitalization
 * and leading or trailing whitespace.
 *
 * Data is not persisted after the application closes.
 */
public class InMemoryFoodDataAccessObject
        implements AddFoodDataAccessInterface,
        ViewFoodBankDataAccessInterface {

    // Stores food items using their unique IDs.
    private final Map<String, FoodItem> foods = new HashMap<>();

    /**
     * Checks whether a food with the specified ID exists.
     *
     * @param foodId ID to check
     * @return true if the ID already exists
     */
    @Override
    public boolean existsById(String foodId) {
        return foods.containsKey(foodId);
    }

    /**
     * Checks whether a food with the specified name exists.
     *
     * Name comparisons ignore capitalization and surrounding spaces.
     *
     * @param name food name to check
     * @return true if an equivalent name exists
     */
    @Override
    public boolean existsByName(String name) {
        if (name == null) {
            return false;
        }

        String normalizedName = normalizeName(name);

        for (FoodItem food : foods.values()) {
            if (normalizeName(food.getName()).equals(normalizedName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a food item to the repository.
     *
     * Rejects duplicate food IDs and names.
     *
     * @param food food item to add
     * @throws IllegalArgumentException if food is null,
     *                                  its ID already exists,
     *                                  or its name already exists
     */
    @Override
    public void addFood(FoodItem food) {
        if (food == null) {
            throw new IllegalArgumentException(
                    "Food cannot be null.");
        }

        if (existsById(food.getId())) {
            throw new IllegalArgumentException(
                    "A food with this ID already exists.");
        }

        if (existsByName(food.getName())) {
            throw new IllegalArgumentException(
                    "A food with this name already exists.");
        }

        foods.put(food.getId(), food);
    }

    /**
     * Retrieves a food item using its unique ID.
     *
     * @param foodId ID of the food to retrieve
     * @return matching food item, or null if none exists
     */
    public FoodItem getFoodById(String foodId) {
        return foods.get(foodId);
    }

    /**
     * Retrieves all foods currently stored in the repository.
     *
     * Returns a new list so callers cannot directly modify
     * the repository's internal collection.
     *
     * @return list of stored food items, or an empty list
     *         if no foods have been added
     */
    @Override
    public List<FoodItem> getAllFoods() {
        return new ArrayList<>(foods.values());
    }

    /**
     * Normalizes food names to support case-insensitive
     * uniqueness checking.
     *
     * @param name food name to normalize
     * @return trimmed, lowercase food name
     */
    private String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}


package data_access;

import entity.FoodItem;
import use_case.add_food.AddFoodDataAccessInterface;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * In-memory implementation of the food data access interface.
 *
 * Stores food items in a HashMap using their unique IDs.
 * Food names must also be unique, ignoring capitalization
 * and leading or trailing whitespace.
 *
 * Data is not persisted after the application closes.
 */
public class InMemoryFoodDataAccessObject
        implements AddFoodDataAccessInterface {

    // Stores food items using their unique IDs as keys.
    private final Map<String, FoodItem> foods = new HashMap<>();

    /**
     * Checks whether a food with the specified ID exists.
     *
     * @param foodId ID to check
     * @return true if the ID exists
     */
    @Override
    public boolean existsById(String foodId) {
        return foods.containsKey(foodId);
    }

    /**
     * Checks whether a food with an equivalent name exists.
     *
     * Comparisons ignore capitalization and surrounding spaces.
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
     * Adds a food item to the in-memory database.
     *
     * Rejects duplicate IDs and food names.
     *
     * @param food food item to add
     * @throws IllegalArgumentException if food is null or
     *                                  its ID or name already exists
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
     * Retrieves a food item using its ID.
     *
     * @param foodId ID of the food to retrieve
     * @return matching food, or null if not found
     */
    public FoodItem getFoodById(String foodId) {
        return foods.get(foodId);
    }

    /**
     * Returns an unmodifiable view of all stored foods.
     *
     * @return map of food IDs to food items
     */
    public Map<String, FoodItem> getAllFoods() {
        return Collections.unmodifiableMap(foods);
    }

    /**
     * Normalizes a food name for consistent comparisons.
     *
     * @param name food name to normalize
     * @return trimmed, lowercase food name
     */
    private String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}

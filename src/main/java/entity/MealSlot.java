package entity;

/**
 * Represents a meal slot in a user's daily meal schedule.
 *
 * A meal slot identifies a category of meal, such as Breakfast,
 * Lunch, Dinner, or Snack.
 *
 * Meal slots are configurable, allowing users to customize
 * the number and names of meals in their daily schedule.
 *
 * This class is immutable.
 */
public class MealSlot {

    // Unique identifier for the meal slot.
    private final String id;

    // Display name of the meal slot.
    private final String name;

    /**
     * Constructs a MealSlot with the specified ID and name.
     *
     * @param id unique identifier of the meal slot
     * @param name display name of the meal slot
     * @throws IllegalArgumentException if ID or name is null or blank
     */
    public MealSlot(String id, String name) {

        // Ensure the meal slot has a valid identifier.
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Meal slot ID cannot be empty.");
        }

        // Ensure the meal slot has a valid display name.
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Meal slot name cannot be empty.");
        }

        this.id = id;
        this.name = name;
    }

    /**
     * Returns the unique identifier of this meal slot.
     *
     * @return meal slot ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the display name of this meal slot.
     *
     * @return meal slot name
     */
    public String getName() {
        return name;
    }
}

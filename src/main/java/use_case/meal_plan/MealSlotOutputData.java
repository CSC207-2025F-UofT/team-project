package use_case.meal_plan;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One cell of the week (a day and a meal type) and the meal planned there, as plain data.
 * An empty cell has an empty recipe name and no ingredients.
 */
public class MealSlotOutputData {

    private final DayOfWeek day;
    private final String mealType;
    private final String recipeName;
    private final boolean cooked;
    private final List<PlannedIngredientOutputData> ingredients;

    /**
     * Creates output data for one cell.
     *
     * @param day the day of the week
     * @param mealType the meal type name, e.g. "BREAKFAST"
     * @param recipeName the planned recipe, or null or empty if nothing is planned
     * @param cooked whether the planned meal has been cooked
     * @param ingredients the ingredients of the planned meal
     */
    public MealSlotOutputData(DayOfWeek day, String mealType, String recipeName, boolean cooked,
                              List<PlannedIngredientOutputData> ingredients) {
        this.day = day;
        this.mealType = mealType;
        this.recipeName = recipeName == null ? "" : recipeName;
        this.cooked = cooked;
        this.ingredients = new ArrayList<>(ingredients);
    }

    /**
     * Returns the day of the week.
     *
     * @return the day
     */
    public DayOfWeek getDay() {
        return day;
    }

    /**
     * Returns the meal type name, e.g. "BREAKFAST".
     *
     * @return the meal type
     */
    public String getMealType() {
        return mealType;
    }

    /**
     * Returns the planned recipe name, or an empty string if nothing is planned.
     *
     * @return the recipe name
     */
    public String getRecipeName() {
        return recipeName;
    }

    /**
     * Returns whether a meal is planned in this cell.
     *
     * @return true if a meal is planned
     */
    public boolean isFilled() {
        return !recipeName.isEmpty();
    }

    /**
     * Returns whether the planned meal has been cooked.
     *
     * @return true if cooked
     */
    public boolean isCooked() {
        return cooked;
    }

    /**
     * Returns the ingredients of the planned meal.
     *
     * @return the ingredients, empty for an empty cell
     */
    public List<PlannedIngredientOutputData> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }
}

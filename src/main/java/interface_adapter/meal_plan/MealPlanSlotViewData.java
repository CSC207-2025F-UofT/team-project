package interface_adapter.meal_plan;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * What the meal plan page shows for one cell of the 7 x 3 grid.
 */
public class MealPlanSlotViewData {

    private final DayOfWeek day;
    private final String mealType;
    private final String recipeName;
    private final boolean cooked;
    private final List<String> ingredientLines;

    /**
     * Creates the view data for one cell.
     *
     * @param day the day of the cell
     * @param mealType the meal of the cell, e.g. "BREAKFAST"
     * @param recipeName the planned recipe, or an empty string if the cell is empty
     * @param cooked whether the planned meal has been cooked
     * @param ingredientLines one line per ingredient, e.g. "Garlic: 2 cloves"
     */
    public MealPlanSlotViewData(DayOfWeek day, String mealType, String recipeName,
                                boolean cooked, List<String> ingredientLines) {
        this.day = day;
        this.mealType = mealType;
        this.recipeName = recipeName == null ? "" : recipeName;
        this.cooked = cooked;
        this.ingredientLines = new ArrayList<>(ingredientLines);
    }

    /**
     * Returns the day of the cell.
     *
     * @return the day
     */
    public DayOfWeek getDay() {
        return day;
    }

    /**
     * Returns the meal of the cell.
     *
     * @return the meal type name
     */
    public String getMealType() {
        return mealType;
    }

    /**
     * Returns the planned recipe name, or an empty string.
     *
     * @return the recipe name
     */
    public String getRecipeName() {
        return recipeName;
    }

    /**
     * Returns whether a meal is planned in this cell.
     *
     * @return true if the cell has a meal
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
     * Returns the ingredient lines for the side panel.
     *
     * @return an unmodifiable list of lines
     */
    public List<String> getIngredientLines() {
        return Collections.unmodifiableList(ingredientLines);
    }
}

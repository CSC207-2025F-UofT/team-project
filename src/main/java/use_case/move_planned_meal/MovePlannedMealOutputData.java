package use_case.move_planned_meal;

import entity.MealPlan;

/**
 * Output data for moving a planned meal.
 */
public class MovePlannedMealOutputData {

    private final MealPlan mealPlan;
    private final String movedRecipeName;

    /**
     * Creates output data.
     *
     * @param mealPlan the week after the move
     * @param movedRecipeName the name of the recipe that was moved
     */
    public MovePlannedMealOutputData(MealPlan mealPlan, String movedRecipeName) {
        this.mealPlan = mealPlan;
        this.movedRecipeName = movedRecipeName;
    }

    /**
     * Returns the updated meal plan.
     *
     * @return the meal plan
     */
    public MealPlan getMealPlan() {
        return mealPlan;
    }

    /**
     * Returns the name of the moved recipe.
     *
     * @return the recipe name
     */
    public String getMovedRecipeName() {
        return movedRecipeName;
    }
}

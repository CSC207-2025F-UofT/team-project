package use_case.remove_planned_meal;

import entity.MealPlan;

/**
 * Output data for removing a planned meal.
 */
public class RemovePlannedMealOutputData {

    private final MealPlan mealPlan;
    private final String removedRecipeName;

    /**
     * Creates output data.
     *
     * @param mealPlan the week after the removal
     * @param removedRecipeName the name of the recipe that was removed
     */
    public RemovePlannedMealOutputData(MealPlan mealPlan, String removedRecipeName) {
        this.mealPlan = mealPlan;
        this.removedRecipeName = removedRecipeName;
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
     * Returns the name of the removed recipe.
     *
     * @return the recipe name
     */
    public String getRemovedRecipeName() {
        return removedRecipeName;
    }
}

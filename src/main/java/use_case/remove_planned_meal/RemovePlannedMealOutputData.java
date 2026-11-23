package use_case.remove_planned_meal;

import use_case.meal_plan.MealPlanWeekOutputData;

/**
 * Output data for removing a planned meal.
 */
public class RemovePlannedMealOutputData {

    private final MealPlanWeekOutputData week;
    private final String removedRecipeName;

    /**
     * Creates output data.
     *
     * @param week the week after the removal
     * @param removedRecipeName the name of the recipe that was removed
     */
    public RemovePlannedMealOutputData(MealPlanWeekOutputData week, String removedRecipeName) {
        this.week = week;
        this.removedRecipeName = removedRecipeName;
    }

    /**
     * Returns the week after the removal.
     *
     * @return the week
     */
    public MealPlanWeekOutputData getWeek() {
        return week;
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

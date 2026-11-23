package use_case.move_planned_meal;

import use_case.meal_plan.MealPlanWeekOutputData;

/**
 * Output data for moving a planned meal.
 */
public class MovePlannedMealOutputData {

    private final MealPlanWeekOutputData week;
    private final String movedRecipeName;

    /**
     * Creates output data.
     *
     * @param week the week after the move
     * @param movedRecipeName the name of the recipe that was moved
     */
    public MovePlannedMealOutputData(MealPlanWeekOutputData week, String movedRecipeName) {
        this.week = week;
        this.movedRecipeName = movedRecipeName;
    }

    /**
     * Returns the week after the move.
     *
     * @return the week
     */
    public MealPlanWeekOutputData getWeek() {
        return week;
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

package use_case.load_meal_plan;

import use_case.meal_plan.MealPlanWeekOutputData;

/**
 * Output data for loading a meal plan.
 */
public class LoadMealPlanOutputData {

    private final MealPlanWeekOutputData week;

    /**
     * Creates output data.
     *
     * @param week the week to show (never null)
     */
    public LoadMealPlanOutputData(MealPlanWeekOutputData week) {
        this.week = week;
    }

    /**
     * Returns the week to show.
     *
     * @return the week
     */
    public MealPlanWeekOutputData getWeek() {
        return week;
    }

    /**
     * Returns whether nothing is planned for the week, so the view can suggest the Recipes page.
     *
     * @return true if no meals are planned
     */
    public boolean isEmpty() {
        return week.isEmpty();
    }
}

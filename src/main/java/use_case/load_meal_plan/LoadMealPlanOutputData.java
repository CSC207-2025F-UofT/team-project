package use_case.load_meal_plan;

import entity.MealPlan;

/**
 * Output data for loading a meal plan.
 */
public class LoadMealPlanOutputData {

    private final MealPlan mealPlan;
    private final boolean empty;

    /**
     * Creates output data.
     *
     * @param mealPlan the week to show (never null)
     * @param empty true if no meals are planned for the week
     */
    public LoadMealPlanOutputData(MealPlan mealPlan, boolean empty) {
        this.mealPlan = mealPlan;
        this.empty = empty;
    }

    /**
     * Returns the meal plan to show.
     *
     * @return the meal plan
     */
    public MealPlan getMealPlan() {
        return mealPlan;
    }

    /**
     * Returns whether nothing is planned for the week, so the view can suggest the Recipes page.
     *
     * @return true if no meals are planned
     */
    public boolean isEmpty() {
        return empty;
    }
}

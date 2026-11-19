package use_case.load_meal_plan;

import entity.MealPlan;

import java.time.LocalDate;

/**
 * Reads the meal plan of one week. Implemented by the meal plan DAO.
 */
public interface LoadMealPlanDataAccessInterface {

    /**
     * Returns the meal plan for the given week, or null if none is saved.
     *
     * @param weekStart the Monday that starts the week
     * @return the saved meal plan, or null
     */
    MealPlan getMealPlan(LocalDate weekStart);
}

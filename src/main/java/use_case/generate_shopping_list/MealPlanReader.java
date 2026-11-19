package use_case.generate_shopping_list;

import entity.MealPlan;

import java.time.LocalDate;

/**
 * Read-only access to meal plans. Implemented later by the meal-plan file DAO.
 */
public interface MealPlanReader {

    /**
     * Returns the meal plan for the given week, or null if none exists.
     *
     * @param weekStart the Monday that starts the week
     * @return the meal plan, or null
     */
    MealPlan getMealPlan(LocalDate weekStart);
}

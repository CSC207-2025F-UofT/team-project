package use_case.move_planned_meal;

import entity.MealPlan;

import java.time.LocalDate;

/**
 * Reads and saves meal plans for moving a meal. Implemented by the meal plan DAO.
 */
public interface MovePlannedMealDataAccessInterface {

    /**
     * Returns the meal plan for the given week, or null if none is saved.
     *
     * @param weekStart the Monday that starts the week
     * @return the saved meal plan, or null
     */
    MealPlan getMealPlan(LocalDate weekStart);

    /**
     * Saves a meal plan, replacing any plan already saved for the same week.
     *
     * @param mealPlan the plan to save
     */
    void saveMealPlan(MealPlan mealPlan);
}

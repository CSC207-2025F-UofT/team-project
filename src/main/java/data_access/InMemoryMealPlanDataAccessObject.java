package data_access;

import entity.MealPlan;
import use_case.load_meal_plan.LoadMealPlanDataAccessInterface;
import use_case.move_planned_meal.MovePlannedMealDataAccessInterface;
import use_case.remove_planned_meal.RemovePlannedMealDataAccessInterface;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps meal plans in memory, one per week. Used for tests and until the file DAO is ready.
 */
public class InMemoryMealPlanDataAccessObject implements LoadMealPlanDataAccessInterface,
        RemovePlannedMealDataAccessInterface, MovePlannedMealDataAccessInterface {

    private final Map<LocalDate, MealPlan> plans = new HashMap<>();

    @Override
    public MealPlan getMealPlan(LocalDate weekStart) {
        return plans.get(weekStart);
    }

    /**
     * Stores a meal plan, replacing any plan already saved for the same week.
     *
     * @param mealPlan the plan to store
     */
    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        plans.put(mealPlan.getWeekStart(), mealPlan);
    }
}

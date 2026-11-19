package data_access;

import entity.MealPlan;
import use_case.generate_shopping_list.MealPlanReader;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory read-only meal plan store used until the file DAO is ready.
 */
public class InMemoryMealPlanReader implements MealPlanReader {

    private final Map<LocalDate, MealPlan> plans = new HashMap<>();

    @Override
    public MealPlan getMealPlan(LocalDate weekStart) {
        return plans.get(weekStart);
    }

    /**
     * Seeds a meal plan for demos and tests.
     *
     * @param mealPlan the plan to store
     */
    public void seed(MealPlan mealPlan) {
        plans.put(mealPlan.getWeekStart(), mealPlan);
    }
}

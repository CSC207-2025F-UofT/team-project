package use_case.meal_planning;

import entity.DailyMealPlan;
import java.util.List;
import entity.FoodItem;
import entity.MealSchedule;

import java.time.LocalDate;

public interface AddPlannedMealDataAccessInterface {

    // Return null when no plan exists for this date.
    DailyMealPlan findPlanByDate(LocalDate date)
            throws AddPlannedMealDataAccessException;
    List<FoodItem> findAllFoods()
            throws AddPlannedMealDataAccessException;
    // Return null when the plan has no associated schedule.
    MealSchedule findScheduleByPlanId(String planId)
            throws AddPlannedMealDataAccessException;

    // Return null when the food ID is not in the local Food Bank.
    FoodItem findFoodById(String foodId)
            throws AddPlannedMealDataAccessException;

    void saveUpdatedPlan(DailyMealPlan plan)
            throws AddPlannedMealDataAccessException;
}
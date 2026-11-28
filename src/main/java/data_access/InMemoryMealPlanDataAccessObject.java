package data_access;

import entity.DailyMealPlan;
import entity.MealSchedule;
import use_case.meal_planning.CreatePlanDataAccessException;
import use_case.meal_planning.CreatePlanDataAccessInterface;

import data_access.InMemoryFoodDataAccessObject;
import entity.FoodItem;
import use_case.meal_planning.AddPlannedMealDataAccessException;
import use_case.meal_planning.AddPlannedMealDataAccessInterface;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class InMemoryMealPlanDataAccessObject
        implements CreatePlanDataAccessInterface,
        AddPlannedMealDataAccessInterface {

    private final Map<LocalDate, DailyMealPlan> plansByDate =
            new HashMap<>();
    private final Map<String, MealSchedule> schedulesByPlanId =
            new HashMap<>();
    private final InMemoryFoodDataAccessObject foodDataAccess;

    public InMemoryMealPlanDataAccessObject(
            InMemoryFoodDataAccessObject foodDataAccess) {
        this.foodDataAccess = foodDataAccess;
    }
    @Override
    public List<FoodItem> findAllFoods() {
        return new ArrayList<>(
                foodDataAccess.getAllFoods());
    }
    @Override
    public DailyMealPlan findPlanByDate(LocalDate date) {
        return plansByDate.get(date);
    }

    @Override
    public MealSchedule findScheduleByPlanId(String planId) {
        return schedulesByPlanId.get(planId);
    }

    @Override
    public FoodItem findFoodById(String foodId) {
        return foodDataAccess.getFoodById(foodId);
    }

    @Override
    public void saveUpdatedPlan(DailyMealPlan plan)
            throws AddPlannedMealDataAccessException {
        if (plan == null) {
            throw new AddPlannedMealDataAccessException(
                    "Plan is required.");
        }

        DailyMealPlan existing = plansByDate.get(plan.getDate());
        if (existing == null
                || !existing.getId().equals(plan.getId())) {
            throw new AddPlannedMealDataAccessException(
                    "The plan to update does not exist.");
        }

        plansByDate.put(plan.getDate(), plan);
    }


    @Override
    public boolean existsByDate(LocalDate date) {
        return plansByDate.containsKey(date);
    }

    @Override
    public void save(DailyMealPlan plan, MealSchedule schedule)
            throws CreatePlanDataAccessException {
        if (plan == null || schedule == null) {
            throw new CreatePlanDataAccessException(
                    "Plan and schedule are required.");
        }
        if (existsByDate(plan.getDate())) {
            throw new CreatePlanDataAccessException(
                    "A plan already exists for this date.");
        }

        plansByDate.put(plan.getDate(), plan);
        schedulesByPlanId.put(plan.getId(), schedule);
    }
}
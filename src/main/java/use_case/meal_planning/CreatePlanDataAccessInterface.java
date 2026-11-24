package use_case.meal_planning;

import entity.DailyMealPlan;
import entity.MealSchedule;

import java.time.LocalDate;

/**
 * Data access operations required to create a daily meal plan.
 */
public interface CreatePlanDataAccessInterface {

    /**
     * Checks whether a plan exists for the selected date.
     */
    boolean existsByDate(LocalDate date)
            throws CreatePlanDataAccessException;

    /**
     * Saves a new plan together with its meal schedule.
     *
     * The schedule must be associated with the plan's ID.
     * Both must be saved together, or neither is saved.
     * An existing plan for the same date must not be overwritten.
     */
    void save(DailyMealPlan plan, MealSchedule schedule)
            throws CreatePlanDataAccessException;
}
package interface_adapter.remove_planned_meal;

import entity.MealType;
import use_case.remove_planned_meal.RemovePlannedMealInputBoundary;
import use_case.remove_planned_meal.RemovePlannedMealInputData;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Controller for the "Remove" button on the meal plan page.
 */
public class RemovePlannedMealController {

    private final RemovePlannedMealInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the remove planned meal interactor
     */
    public RemovePlannedMealController(RemovePlannedMealInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Removes the meal in one cell of the week.
     *
     * @param weekStart the Monday of the week shown
     * @param day the day of the cell
     * @param mealType the meal of the cell, e.g. "LUNCH"
     */
    public void execute(LocalDate weekStart, DayOfWeek day, String mealType) {
        interactor.execute(new RemovePlannedMealInputData(weekStart, day, MealType.valueOf(mealType)));
    }
}

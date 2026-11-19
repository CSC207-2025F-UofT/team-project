package interface_adapter.move_planned_meal;

import entity.MealType;
import use_case.move_planned_meal.MovePlannedMealInputBoundary;
import use_case.move_planned_meal.MovePlannedMealInputData;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Controller for the "Move" button on the meal plan page.
 */
public class MovePlannedMealController {

    private final MovePlannedMealInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the move planned meal interactor
     */
    public MovePlannedMealController(MovePlannedMealInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Moves the meal in one cell to another cell of the same week.
     *
     * @param weekStart the Monday of the week shown
     * @param fromDay the day of the cell the meal is in
     * @param fromMealType the meal of the cell the meal is in, e.g. "LUNCH"
     * @param toDay the day of the target cell
     * @param toMealType the meal of the target cell
     */
    public void execute(LocalDate weekStart, DayOfWeek fromDay, String fromMealType,
                        DayOfWeek toDay, String toMealType) {
        interactor.execute(new MovePlannedMealInputData(weekStart,
                fromDay, MealType.valueOf(fromMealType), toDay, MealType.valueOf(toMealType)));
    }
}

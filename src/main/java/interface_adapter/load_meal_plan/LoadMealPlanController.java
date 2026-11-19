package interface_adapter.load_meal_plan;

import use_case.load_meal_plan.LoadMealPlanInputBoundary;
import use_case.load_meal_plan.LoadMealPlanInputData;

import java.time.LocalDate;

/**
 * Controller for loading the week shown on the meal plan page.
 */
public class LoadMealPlanController {

    private final LoadMealPlanInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the load meal plan interactor
     */
    public LoadMealPlanController(LoadMealPlanInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads the week containing the given day.
     *
     * @param date any day of the week to show
     */
    public void execute(LocalDate date) {
        interactor.execute(new LoadMealPlanInputData(date));
    }
}

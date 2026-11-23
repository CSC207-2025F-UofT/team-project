package use_case.load_meal_plan;

import java.time.LocalDate;

/**
 * Input data for loading a meal plan.
 */
public class LoadMealPlanInputData {

    private final LocalDate date;

    /**
     * Creates input data.
     *
     * @param date any day of the week to load; the interactor moves it back to that week's Monday
     */
    public LoadMealPlanInputData(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the requested day.
     *
     * @return the requested day
     */
    public LocalDate getDate() {
        return date;
    }
}

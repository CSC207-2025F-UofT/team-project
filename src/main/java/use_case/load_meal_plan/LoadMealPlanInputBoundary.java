package use_case.load_meal_plan;

/**
 * Input boundary for loading the meal plan of a week (use case 3a).
 */
public interface LoadMealPlanInputBoundary {

    /**
     * Loads the meal plan for the requested week.
     *
     * @param inputData the input data
     */
    void execute(LoadMealPlanInputData inputData);
}

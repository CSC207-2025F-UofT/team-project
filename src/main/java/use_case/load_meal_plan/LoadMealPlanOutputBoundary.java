package use_case.load_meal_plan;

/**
 * Output boundary for loading a meal plan.
 */
public interface LoadMealPlanOutputBoundary {

    /**
     * Presents the loaded week. Called even when nothing is planned, with an empty plan.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(LoadMealPlanOutputData outputData);
}

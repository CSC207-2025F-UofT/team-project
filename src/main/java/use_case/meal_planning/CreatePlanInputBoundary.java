package use_case.meal_planning;

/**
 * Input boundary for creating a daily meal plan.
 */
public interface CreatePlanInputBoundary {

    /**
     * Creates a daily meal plan using the supplied input.
     *
     * @param inputData the selected date and meal slot names
     */
    void execute(CreatePlanInputData inputData);
}

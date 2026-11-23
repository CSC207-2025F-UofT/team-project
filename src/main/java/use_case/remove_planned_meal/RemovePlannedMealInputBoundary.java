package use_case.remove_planned_meal;

/**
 * Input boundary for removing a planned meal from the week (use case 3b).
 */
public interface RemovePlannedMealInputBoundary {

    /**
     * Removes the meal in the requested slot.
     *
     * @param inputData the input data
     */
    void execute(RemovePlannedMealInputData inputData);
}

package use_case.move_planned_meal;

/**
 * Input boundary for moving a planned meal to another slot (use case 3b).
 */
public interface MovePlannedMealInputBoundary {

    /**
     * Moves the meal from one slot to another.
     *
     * @param inputData the input data
     */
    void execute(MovePlannedMealInputData inputData);
}

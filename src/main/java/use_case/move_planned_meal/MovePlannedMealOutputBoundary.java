package use_case.move_planned_meal;

/**
 * Output boundary for moving a planned meal.
 */
public interface MovePlannedMealOutputBoundary {

    /**
     * Presents the week after the meal was moved.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(MovePlannedMealOutputData outputData);

    /**
     * Presents why the meal could not be moved.
     *
     * @param errorMessage the message to show the user
     */
    void prepareFailView(String errorMessage);
}

package use_case.remove_planned_meal;

/**
 * Output boundary for removing a planned meal.
 */
public interface RemovePlannedMealOutputBoundary {

    /**
     * Presents the week after the meal was removed.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(RemovePlannedMealOutputData outputData);

    /**
     * Presents why the meal could not be removed.
     *
     * @param errorMessage the message to show the user
     */
    void prepareFailView(String errorMessage);
}

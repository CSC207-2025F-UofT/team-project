package use_case.meal_planning;

/**
 * Output boundary for presenting the result of creating a plan.
 */
public interface CreatePlanOutputBoundary {

    /**
     * Presents a successfully saved plan.
     *
     * @param outputData the saved plan's ID and date
     */
    void prepareSuccessView(CreatePlanOutputData outputData);

    /**
     * Presents a failure without reporting success.
     *
     * @param errorMessage a description of the failure
     */
    void prepareFailView(String errorMessage);
}

package use_case.initialise_predictions;

/**
 * Output Boundary for the Initialize Predictions use case.
 * Presenter implements this to display or store prediction results.
 */
public interface InitialisePredictionsOutputBoundary {
    /**
     * Present successful prediction initialization.
     *
     * @param outputData Contains all players with calculated predictions
     */
    void presentSuccess(InitialisePredictionsOutputData outputData);

    /**
     * Present failure in prediction initialization.
     *
     * @param errorMessage Description of what went wrong
     */
    void presentFailure(String errorMessage);
}
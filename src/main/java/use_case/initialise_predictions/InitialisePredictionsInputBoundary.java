package use_case.initialise_predictions;

/**
 * Input Boundary for the Initialize Predictions use case.
 * This is executed once at application startup to load and prepare
 * all player predictions.
 */
public interface InitialisePredictionsInputBoundary {
    /**
     * Execute the initialize predictions use case.
     * Fetches all necessary data, calculates features, and generates predictions.
     *
     * @param inputData Contains configuration for prediction initialization
     */
    void execute(InitialisePredictionsInputData inputData);
}
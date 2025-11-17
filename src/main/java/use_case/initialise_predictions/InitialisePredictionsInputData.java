package use_case.initialise_predictions;

/**
 * Input Data for Initialize Predictions use case.
 * Currently empty but can be extended to include configuration parameters
 * like which gameweeks to process, caching options, etc.
 */
public class InitialisePredictionsInputData {
    // For now, this can be empty since we process all available data
    // Future extensions could include:
    // - int maxGameweeks (limit how many gameweeks to process)
    // - boolean useCache (whether to use cached data)
    // - String season (which season to predict for)

    public InitialisePredictionsInputData() {
        // Default constructor
    }
}
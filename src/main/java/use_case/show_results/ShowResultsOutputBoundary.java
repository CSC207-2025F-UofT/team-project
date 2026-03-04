package use_case.show_results;

/**
 * Output boundary for showing results.
 */
public interface ShowResultsOutputBoundary {
    /**
     * Prepares the success view.
     * @param outputData the output data
     */
    void prepareSuccessView(ShowResultsOutputData outputData);
}

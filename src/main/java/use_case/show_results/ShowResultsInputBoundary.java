package use_case.show_results;

/**
 * Input boundary for showing results.
 */
public interface ShowResultsInputBoundary {
    /**
     * Executes the show results use case.
     * @param showResultsInputData the input data
     */
    void execute(ShowResultsInputData showResultsInputData);
}

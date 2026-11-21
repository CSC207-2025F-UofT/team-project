package use_case.removeBook;

/**
 * Output Boundary for the Remove Book use case.
 */
public interface RemoveBookOutputBoundary {
    /**
     * Prepares the success view for the Remove Book use case.
     * @param outputData the output data
     */
    void prepareSuccessView(RemoveBookOutputData outputData);

    /**
     * Prepares the failure view for the Remove Book use case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}

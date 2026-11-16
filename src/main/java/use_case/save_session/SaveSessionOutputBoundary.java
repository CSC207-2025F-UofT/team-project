package use_case.save_session;

/**
 * The output boundary for the Save Session Use Case.
 */
public interface SaveSessionOutputBoundary {
    /**
     * Prepares the success view for the Save Session Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(SaveSessionOutputData outputData);
    /**
     * Prepares the fail view for the Save Session Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}

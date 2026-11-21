package use_case.job_match;

/**
 * The output boundary for the Job Match Use Case.
 */
public interface JobMatchOutputBoundary {
    /**
     * Prepares the success view for the Job Match Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(JobMatchOutputData outputData);

    /**
     * Prepares the failure view for the Job Match Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}

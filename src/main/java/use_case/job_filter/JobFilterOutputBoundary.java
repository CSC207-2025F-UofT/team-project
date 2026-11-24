package use_case.job_filter;

/**
 * The output boundary for the Job Filter Use Case.
 */
public interface JobFilterOutputBoundary {
    /**
     * Prepares the success view for the Job Filter Use Case.
     * @param outputData matching jobs, possibly empty
     */
    void prepareSuccessView(JobFilterOutputData outputData);

    /**
     * Prepares the failure view for the Job Filter Use Case.
     * @param errorMessage the reason the filter criteria could not be applied
     */
    void prepareFailView(String errorMessage);
}

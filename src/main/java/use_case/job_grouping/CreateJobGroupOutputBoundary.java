package use_case.job_grouping;

/**
 * The output boundary for the Create Job Group use case.
 */
public interface CreateJobGroupOutputBoundary {

    /**
     * Prepares the success view after creating a job group.
     * @param outputData the output data
     */
    void prepareSuccessView(CreateJobGroupOutputData outputData);

    /**
     * Prepares the failure view if a job group cannot be created.
     * @param errorMessage the error message
     */
    void prepareFailView(String errorMessage);
}
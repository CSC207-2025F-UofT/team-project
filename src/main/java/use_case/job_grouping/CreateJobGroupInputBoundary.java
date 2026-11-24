package use_case.job_grouping;

/**
 * The input boundary for the Create Job Group use case.
 */
public interface CreateJobGroupInputBoundary {

    /**
     * Executes the Create Job Group use case.
     * @param createJobGroupInputData the input data
     */
    void execute(CreateJobGroupInputData createJobGroupInputData);
}

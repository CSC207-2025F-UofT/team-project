package use_case.job_match;

/**
 * The Job Match Use Case.
 */
public interface JobMatchInputBoundary {

    /**
     * Execute the Job Match Use Case.
     * @param jobMatchInputData the input data for this use case
     */
    void execute(JobMatchInputData jobMatchInputData);

}

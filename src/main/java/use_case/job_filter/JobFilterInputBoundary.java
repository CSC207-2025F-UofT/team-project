package use_case.job_filter;

/**
 * The Job Filter Use Case.
 */
public interface JobFilterInputBoundary {

    /**
     * Filter the jobs in the supplied student's job groups.
     * @param jobFilterInputData the input data for this use case
     */
    void execute(JobFilterInputData jobFilterInputData);

}

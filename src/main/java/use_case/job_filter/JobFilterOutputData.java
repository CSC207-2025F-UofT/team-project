package use_case.job_filter;

import entity.Job;
import java.util.List;

/**
 * Output Data for the Job Filter Use Case.
 */
public class JobFilterOutputData {

    private final List<Job> jobs;

    public JobFilterOutputData(List<Job> jobs) {
        this.jobs = List.copyOf(jobs);
    }

    public List<Job> getJobs() {
        return jobs;
    }
}

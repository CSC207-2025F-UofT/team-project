package interface_adapter.job_filter;

import entity.Job;
import java.util.List;

public class JobFilterState {
    private final List<Job> jobs;
    private final String errorMessage;

    JobFilterState(List<Job> jobs, String errorMessage) {
        this.jobs = List.copyOf(jobs);
        this.errorMessage = errorMessage;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

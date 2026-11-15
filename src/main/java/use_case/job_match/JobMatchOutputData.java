package use_case.job_match;

import entity.Job;

import java.util.ArrayList;

/**
 * Output Data for the Job Match Use Case.
 */
public class JobMatchOutputData {

    private final ArrayList<Job> jobs;

    public JobMatchOutputData(ArrayList<Job> jobs) {
        this.jobs = new ArrayList<>(jobs);
    }

    public ArrayList<Job> getJobs() {
        return new ArrayList<>(jobs);
    }
}

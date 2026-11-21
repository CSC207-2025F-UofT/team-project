package interface_adapter.job_list;

import entity.Job;

import java.util.ArrayList;

/**
 * The state for the JobList view model.
 */
public class JobListState {
    private ArrayList<Job> jobs = new ArrayList<>();
    private String error;

    public ArrayList<Job> getJobs() {
        return new ArrayList<>(jobs);
    }

    public String getError() {
        return error;
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = new ArrayList<>(jobs);
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "JobListState{jobs=" + jobs + ", error='" + error + '\'' + '}';
    }
}

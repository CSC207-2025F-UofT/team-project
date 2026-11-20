package use_case.job_grouping;

import entity.Job;
import java.util.ArrayList;

/**
 * The input data for creating a job group.
 */
public class CreateJobGroupInputData {

    private final String username;
    private final String groupName;
    private final ArrayList<Job> jobs;

    /**
     * Creates the input data for a new job group.
     * @param username the student creating the group
     * @param groupName the name of the group
     * @param jobs the selected jobs
     */
    public CreateJobGroupInputData(String username, String groupName,
                                   ArrayList<Job> jobs) {
        this.username = username;
        this.groupName = groupName;
        this.jobs = new ArrayList<>(jobs);
    }

    public String getUsername() {
        return username;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<Job> getJobs() {
        return new ArrayList<>(jobs);
    }
}

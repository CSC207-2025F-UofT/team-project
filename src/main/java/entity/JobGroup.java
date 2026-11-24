package entity;

import java.util.ArrayList;

/**
 * A simple entity representing a group of jobs.
 * Each job group has a name and a list of jobs.
 */
public class JobGroup {

    private String name;
    private ArrayList<Job> jobs;

    /**
     * Creates a new job group with the given name and jobs.
     * @param name the name of the job group
     * @param jobs the jobs in the group
     */
    public JobGroup(String name, ArrayList<Job> jobs) {
        this.name = name;
        this.jobs = new ArrayList<>(jobs);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Job> getJobs() {
        return new ArrayList<>(jobs);
    }

    /**
     * Adds a job to the group.
     * @param job the job to add
     */
    public void addJob(Job job) {
        if (!jobs.contains(job)) {
            jobs.add(job);
        }
    }
}

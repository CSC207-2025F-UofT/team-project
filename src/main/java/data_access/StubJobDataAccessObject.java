package data_access;

import entity.Job;
import entity.Student;
import use_case.job_match.JobMatchUserDataAccessInterface;

import java.util.ArrayList;

/**
 * Placeholder job DAO that returns no jobs.
 * TODO: replace with a DAO that fetches jobs from a real job listing API.
 */
public class StubJobDataAccessObject implements JobMatchUserDataAccessInterface {

    @Override
    public ArrayList<Job> getJobs(Student student) {
        return new ArrayList<>();
    }
}

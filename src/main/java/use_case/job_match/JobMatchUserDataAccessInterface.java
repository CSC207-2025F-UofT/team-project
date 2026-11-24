package use_case.job_match;

import entity.Job;
import entity.Student;

import java.util.ArrayList;

/**
 * The DAO interface for the Job Match Use Case.
 */
public interface JobMatchUserDataAccessInterface {

    /**
     * Finds jobs matching the given student's courses and skills.
     * @param student the student whose profile is used for matching
     * @return the matching jobs, or an empty list if none match
     * @throws JobFetchException if the jobs could not be fetched
     */
    ArrayList<Job> getJobs(Student student);
}

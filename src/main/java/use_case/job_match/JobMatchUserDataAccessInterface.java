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
     */
    ArrayList<Job> getJobs(Student student);
}

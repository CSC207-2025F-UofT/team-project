package use_case.job_match;

import entity.Student;

/**
 * The DAO interface for the Job Match Use Case.
 */
public interface JobMatchUserDataAccessInterface {

    /**
     * Updates the system to record this user's password as part of the Job Match Use Case.
     * @param student the user whose password is to be updated
     */
    void changePassword(Student student);
}

package use_case.job_match;

import entity.Student;

/**
 * The DAO interface for looking up students in the Job Match Use Case.
 */
public interface JobMatchStudentDataAccessInterface {

    /**
     * Returns the student with the given username.
     * @param username the username to look up
     * @return the student, or null if there is no such student
     */
    Student get(String username);
}

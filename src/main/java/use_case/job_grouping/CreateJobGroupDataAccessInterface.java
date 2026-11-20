package use_case.job_grouping;

import entity.Student;

/**
 * The data access interface for creating job groups.
 */
public interface CreateJobGroupDataAccessInterface {

    /**
     * Returns the student with the given username.
     * @param username the student's username
     * @return the student
     */
    Student getStudent(String username);

    /**
     * Saves the student's updated information.
     * @param student the student to save
     */
    void save(Student student);
}

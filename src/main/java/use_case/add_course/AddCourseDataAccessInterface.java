package use_case.add_course;

import entity.Student;

/**
 * DAO interface for the Add Course Use Case.
 */
public interface AddCourseDataAccessInterface {

    /**
     * Returns the username of the student who is currently logged in.
     * @return the current username
     */
    String getCurrentUsername();

    /**
     * Returns the student with the given username.
     * @param username the username to look up
     * @return the student
     */
    Student get(String username);

    /**
     * Saves the student.
     * @param student the student to save
     */
    void save(Student student);
}
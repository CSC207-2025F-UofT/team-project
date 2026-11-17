package use_case.enrol_course;

import entity.Course;

/**
 * DAO interface for the Enrol Course Use Case.
 */
public interface EnrolCourseDataAccessInterface {

    /**
     * Checks whether the user is already enrolled in a course with the given code.
     * @param username the user to look up
     * @param courseCode the course code, compared ignoring case
     * @return true if the user already has a course with this code; false otherwise
     */
    boolean existsByCode(String username, String courseCode);

    /**
     * Saves a course for the given user.
     * @param username the user who owns the course
     * @param course the course to save
     */
    void saveCourse(String username, Course course);
}
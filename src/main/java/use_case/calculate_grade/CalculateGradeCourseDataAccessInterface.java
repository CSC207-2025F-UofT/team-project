package use_case.calculate_grade;

import entity.Course;

/**
 * DAO interface for the Calculate Grade Use Case.
 */
public interface CalculateGradeCourseDataAccessInterface {

    /**
     * Returns one of the user's courses.
     * @param username the owner of the course
     * @param courseCode the code of the course
     * @return the course, or null if the user is not enrolled in a course with that code
     */
    Course getCourse(String username, String courseCode);

    /**
     * Saves the user's course, replacing any course with the same code.
     * @param username the owner of the course
     * @param course the course to save
     */
    void saveCourse(String username, Course course);
}

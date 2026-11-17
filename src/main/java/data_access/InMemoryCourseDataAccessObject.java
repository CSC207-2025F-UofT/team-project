package data_access;

import entity.Course;
import use_case.enrol_course.EnrolCourseDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing each user's courses. This
 * implementation does NOT persist data between runs of the program; it is used
 * by the interactor tests and until the persistent course DAO is ready.
 * Other course use cases add their data access interface to the implements list.
 */
public class InMemoryCourseDataAccessObject implements EnrolCourseDataAccessInterface {

    /** username -> (upper-case course code -> course), in the order the courses were added. */
    private final Map<String, Map<String, Course>> coursesByUser = new HashMap<>();

    @Override
    public boolean existsByCode(String username, String courseCode) {
        final Map<String, Course> courses = coursesByUser.get(username);
        return courses != null && courses.containsKey(key(courseCode));
    }

    @Override
    public void saveCourse(String username, Course course) {
        coursesByUser.computeIfAbsent(username, user -> new LinkedHashMap<>())
                .put(key(course.getCode()), course);
    }

    /**
     * Returns the user's course with the given code.
     * @param username the user who owns the course
     * @param courseCode the course code, compared ignoring case
     * @return the course, or null if the user has no course with this code
     */
    public Course getCourse(String username, String courseCode) {
        final Map<String, Course> courses = coursesByUser.get(username);
        if (courses == null) {
            return null;
        }
        return courses.get(key(courseCode));
    }

    /**
     * Returns all of the user's courses, in the order they were enrolled.
     * @param username the user who owns the courses
     * @return a new list of the user's courses, empty if they have none
     */
    public List<Course> getCourses(String username) {
        final Map<String, Course> courses = coursesByUser.get(username);
        if (courses == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(courses.values());
    }

    private static String key(String courseCode) {
        return courseCode.trim().toUpperCase(Locale.ROOT);
    }
}

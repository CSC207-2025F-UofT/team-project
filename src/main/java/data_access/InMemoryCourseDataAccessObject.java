package data_access;

import entity.Course;
import use_case.calculate_grade.CalculateGradeCourseDataAccessInterface;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing each user's courses. This implementation
 * does NOT persist data between runs of the program. Course codes are matched ignoring case.
 * Other course use cases add their data access interfaces to the implements list.
 */
public class InMemoryCourseDataAccessObject implements CalculateGradeCourseDataAccessInterface {

    private final Map<String, Map<String, Course>> coursesByUser = new HashMap<>();

    @Override
    public Course getCourse(String username, String courseCode) {
        final Map<String, Course> courses = coursesByUser.get(username);
        if (courses == null || courseCode == null) {
            return null;
        }
        return courses.get(key(courseCode));
    }

    @Override
    public void saveCourse(String username, Course course) {
        coursesByUser.computeIfAbsent(username, name -> new LinkedHashMap<>())
                .put(key(course.getCode()), course);
    }

    private static String key(String courseCode) {
        return courseCode.trim().toUpperCase(Locale.ROOT);
    }
}

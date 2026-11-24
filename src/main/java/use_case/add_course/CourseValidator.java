package use_case.add_course;

/**
 * Checks whether a course exists at UofT.
 */
public interface CourseValidator {

    /**
     * Returns the full course code (e.g. "CSC207H1"), or null if the course does not exist.
     * @return the full course code, or null
     */
    String findFullCode(String courseCode);
}
package use_case.add_course;

/**
 * Checks whether a course exists at UofT.
 */
public interface CourseValidator {

    /**
     * Returns true if the given course code is a real UofT course.
     * Returns false if the course does not exist or could not be checked.
     * @param courseCode a normalized course code, e.g. "CSC207" or "CSC207H1"
     * @return true if the course exists, false otherwise
     */
    boolean courseExists(String courseCode);
}
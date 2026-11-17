package use_case.enrol_course;

/**
 * Input Boundary for actions which are related to enrolling in a course.
 */
public interface EnrolCourseInputBoundary {

    /**
     * Executes the enrol course use case.
     * @param enrolCourseInputData the input data
     */
    void execute(EnrolCourseInputData enrolCourseInputData);

    /**
     * Opens the form for entering a new course.
     */
    void switchToEnrolView();

    /**
     * Leaves the form and goes back to the home view without enrolling.
     */
    void switchToHomeView();
}
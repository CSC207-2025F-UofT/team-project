package interface_adapter.enrol_course;

/**
 * The state for the Enrol View Model.
 */
public class EnrolState {
    private String errorMessage;
    private String enrolledCourseCode;

    /**
     * Returns why the last enrol attempt failed.
     * @return the error message, or null if the last attempt did not fail
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Returns the code of the course that was just enrolled in.
     * @return the course code, or null if the last attempt did not succeed
     */
    public String getEnrolledCourseCode() {
        return enrolledCourseCode;
    }

    public void setEnrolledCourseCode(String enrolledCourseCode) {
        this.enrolledCourseCode = enrolledCourseCode;
    }
}

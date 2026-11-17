package use_case.enrol_course;

/**
 * The output boundary for the Enrol Course Use Case.
 */
public interface EnrolCourseOutputBoundary {

    /**
     * Prepares the success view for the Enrol Course Use Case.
     * @param outputData the course that was just saved
     */
    void prepareSuccessView(EnrolCourseOutputData outputData);

    /**
     * Prepares the failure view for the Enrol Course Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Enrol View.
     */
    void switchToEnrolView();

    /**
     * Switches back to the Home View.
     */
    void switchToHomeView();
}


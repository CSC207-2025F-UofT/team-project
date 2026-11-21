package use_case.course_analysis;

/**
 * The output boundary for the Course Analysis Use Case.
 */
public interface AnalysisOutputBoundary {
    /**
     * Prepares the success view for the Course Analysis Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(AnalysisOutputData outputData);

    /**
     * Prepares the failure view for the Course Analysis Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Logged In View.
     */
    void switchToLoggedInView();
}

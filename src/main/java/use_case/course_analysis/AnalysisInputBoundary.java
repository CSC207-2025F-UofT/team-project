package use_case.course_analysis;

/**
 * The input boundary for the Course Analysis Use Case.
 */
public interface AnalysisInputBoundary {

    /**
     * Executes the Course Analysis Use Case.
     * @param analysisInputData the input data for this use case
     */
    void execute(AnalysisInputData analysisInputData);

    /**
     * Executes the "switch to LoggedInView" Use Case.
     */
    void switchToLoggedInView();
}

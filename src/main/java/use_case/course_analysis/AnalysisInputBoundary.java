package use_case.course_analysis;

/**
 * The Change Password Use Case.
 */
public interface AnalysisInputBoundary {

    /**
     * Execute the Change Password Use Case.
     * @param analysisInputData the input data for this use case
     */
    void execute(AnalysisInputData analysisInputData);

    void switchToSkillListView();

}

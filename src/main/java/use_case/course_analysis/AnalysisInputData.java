package use_case.course_analysis;

/**
 * The input data for the Course Analysis Use Case.
 */
public class AnalysisInputData {

    private final String username;

    public AnalysisInputData(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }
}

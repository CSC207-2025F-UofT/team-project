package use_case.course_analysis;

/**
 * Output Data for the Change Password Use Case.
 */
public class AnalysisOutputData {

    private final String username;

    public AnalysisOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

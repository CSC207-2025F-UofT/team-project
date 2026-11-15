package use_case.course_analysis;

/**
 * The input data for the Change Password Use Case.
 */
public class AnalysisInputData {

    private final String password;
    private final String username;

    public AnalysisInputData(String password, String username) {
        this.password = password;
        this.username = username;
    }

    String getPassword() {
        return password;
    }

    String getUsername() {
        return username;
    }

}

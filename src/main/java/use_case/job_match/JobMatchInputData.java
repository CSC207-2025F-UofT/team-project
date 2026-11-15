package use_case.job_match;

/**
 * The input data for the Job Match Use Case.
 */
public class JobMatchInputData {

    private final String password;
    private final String username;

    public JobMatchInputData(String password, String username) {
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

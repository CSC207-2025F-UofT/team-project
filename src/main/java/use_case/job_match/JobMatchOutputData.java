package use_case.job_match;

/**
 * Output Data for the Job Match Use Case.
 */
public class JobMatchOutputData {

    private final String username;

    public JobMatchOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

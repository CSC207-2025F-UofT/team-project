package use_case.job_match;

/**
 * The input data for the Job Match Use Case.
 */
public class JobMatchInputData {

    private final String username;

    public JobMatchInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}

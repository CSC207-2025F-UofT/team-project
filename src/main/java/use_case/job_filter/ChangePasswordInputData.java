package use_case.job_filter;

/**
 * The input data for the Change Password Use Case.
 */
public class ChangePasswordInputData {

    private final String password;
    private final String username;

    public ChangePasswordInputData(String password, String username) {
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

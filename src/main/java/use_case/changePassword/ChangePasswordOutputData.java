package use_case.changePassword;

/**
 * Output Data for the Change Password Use Case.
 */
public class ChangePasswordOutputData {

    private final String username;

    public ChangePasswordOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

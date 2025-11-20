package use_case.change_password;

/**
 * The input data for the Change Password Use Case.
 */
public class ChangePasswordInputData {

    private final String email;
    private final String password;
    private final String username;

    public ChangePasswordInputData(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    String getEmail() { return email; }

    String getPassword() {
        return password;
    }

    String getUsername() {
        return username;
    }

}

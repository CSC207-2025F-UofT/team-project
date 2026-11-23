package use_case.admin_signup;

/**
 * The Input Data for the Admin Signup Use Case.
 * Admins have no preference, so there is no preference field.
 * The invite code proves the person is allowed to become an admin.
 */
public class AdminSignupInputData {

    private final String username;
    private final String id;
    private final String email;
    private final String password;
    private final String repeatPassword;
    private final String inviteCode;

    public AdminSignupInputData(String username, String id, String email, String password,
                                String repeatPassword, String inviteCode) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.inviteCode = inviteCode;
    }

    String getUsername() {
        return username;
    }

    String getId() {
        return id;
    }

    String getEmail() {
        return email;
    }

    String getPassword() {
        return password;
    }

    String getRepeatPassword() {
        return repeatPassword;
    }

    String getInviteCode() {
        return inviteCode;
    }
}

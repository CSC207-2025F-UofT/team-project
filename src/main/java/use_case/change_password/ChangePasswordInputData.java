package use_case.change_password;

/**
 * The input data for the Change Password Use Case.
 * What the user typed into the three password fields.
 */
public class ChangePasswordInputData {

    private final String currentPassword;
    private final String newPassword;
    private final String confirmPassword;

    public ChangePasswordInputData(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    String getCurrentPassword() {
        return currentPassword;
    }

    String getNewPassword() {
        return newPassword;
    }

    String getConfirmPassword() {
        return confirmPassword;
    }
}

package use_case.changePassword;

import entity.user.User;

/**
 * The input data for the Change Password Use Case.
 */
public class ChangePasswordInputData {

    private final String password;
    private final String repeatPassword;

    public ChangePasswordInputData(String password, String repeatPassword) {
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    String getPassword() {
        return password;
    }

    String getRepeatPassword() { return repeatPassword; }


}

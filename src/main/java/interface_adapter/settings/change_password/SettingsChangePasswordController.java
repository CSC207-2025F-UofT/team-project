package interface_adapter.settings.change_password;

import use_case.changePassword.ChangePasswordInputBoundary;
import use_case.changePassword.ChangePasswordInputData;

public class SettingsChangePasswordController {
    private final ChangePasswordInputBoundary userChangePasswordUseCaseInteractor;

    public SettingsChangePasswordController(ChangePasswordInputBoundary userChangePasswordUseCaseInteractor) {
        this.userChangePasswordUseCaseInteractor = userChangePasswordUseCaseInteractor;
    }

    /**
     * Executes the Change Password Use Case.
     * @param password the new password
     * @param repeatPassword the repeated password
     */
    public void changePassword(String password, String repeatPassword) {
        final ChangePasswordInputData changePasswordInputData = new ChangePasswordInputData(password,  repeatPassword);
        userChangePasswordUseCaseInteractor.execute(changePasswordInputData);
    }
}

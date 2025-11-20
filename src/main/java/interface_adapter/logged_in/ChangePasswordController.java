package interface_adapter.logged_in;

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInputData;

/**
 * Controller for the Change Password Use Case.
 */
public class ChangePasswordController {
    private final ChangePasswordInputBoundary userChangePasswordUseCaseInteractor;

    public ChangePasswordController(ChangePasswordInputBoundary userChangePasswordUseCaseInteractor) {
        this.userChangePasswordUseCaseInteractor = userChangePasswordUseCaseInteractor;
    }

    /**
     * Executes the Change Password Use Case.
     * @param email the email of the user
     * @param password the new password
     * @param username the user whose password to change
     */
    public void execute(String email, String password, String username) {
        final ChangePasswordInputData changePasswordInputData = new ChangePasswordInputData(email, password,username);

        userChangePasswordUseCaseInteractor.execute(changePasswordInputData);
    }
}

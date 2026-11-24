package interface_adapter.settings;

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInputData;

/**
 * Controller for the Change Password Use Case.
 */
public class ChangePasswordController {
    private final ChangePasswordInputBoundary changePasswordUseCaseInteractor;

    public ChangePasswordController(ChangePasswordInputBoundary changePasswordUseCaseInteractor) {
        this.changePasswordUseCaseInteractor = changePasswordUseCaseInteractor;
    }

    /** Executes the Change Password Use Case with what the user typed. */
    public void execute(String currentPassword, String newPassword, String confirmPassword) {
        // Same order as ChangePasswordInputData's constructor (ca-login mixes its two up, so watch this)
        final ChangePasswordInputData changePasswordInputData =
                new ChangePasswordInputData(currentPassword, newPassword, confirmPassword);

        changePasswordUseCaseInteractor.execute(changePasswordInputData);
    }
}

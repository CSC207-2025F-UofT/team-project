package use_case.change_password;

import entity.User;
import entity.UserFactory;

/**
 * The Change Password Interactor.
 */
public class ChangePasswordInteractor implements ChangePasswordInputBoundary {

    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final ChangePasswordOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface changePasswordDataAccessInterface,
            ChangePasswordOutputBoundary changePasswordOutputBoundary,
            UserFactory userFactory) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.userPresenter = changePasswordOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(ChangePasswordInputData changePasswordInputData) {
        final String newPassword = changePasswordInputData.getPassword();
        final String username = changePasswordInputData.getUsername();

        if ("".equals(newPassword)) {
            userPresenter.prepareFailView("New password cannot be empty");
            return;
        }

        // Strong password check
        final String passwordError = PasswordValidator.validate(newPassword);
        if (passwordError != null) {
            userPresenter.prepareFailView(passwordError);
            return;
        }

        final User existingUser = userDataAccessObject.get(username);

        // Hash the new password
        final String newPasswordHash = UserFactory.hashPasswordSHA256(newPassword);

        final User updatedUser = userFactory.loadUser(
                existingUser.getUsername(),
                newPasswordHash
        );

        // Save the updated user
        userDataAccessObject.changePassword(updatedUser);

        // Prepare success response
        final ChangePasswordOutputData changePasswordOutputData = new ChangePasswordOutputData(updatedUser.getUsername());
        userPresenter.prepareSuccessView(changePasswordOutputData);
    }
}

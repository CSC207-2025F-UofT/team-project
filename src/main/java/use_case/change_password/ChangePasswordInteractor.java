package use_case.change_password;

import entity.User;
import use_case.account.AccountPassword;
import use_case.account.CurrentUserReader;

import java.util.Optional;

/**
 * The Change Password Interactor (#22).
 * Checks the typed passwords follow the rules, then asks the account service to change it.
 */
public class ChangePasswordInteractor implements ChangePasswordInputBoundary {
    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final CurrentUserReader currentUserReader;
    private final ChangePasswordOutputBoundary userPresenter;

    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface changePasswordDataAccessInterface,
                                    CurrentUserReader currentUserReader,
                                    ChangePasswordOutputBoundary changePasswordOutputBoundary) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.currentUserReader = currentUserReader;
        this.userPresenter = changePasswordOutputBoundary;
    }

    @Override
    public void execute(ChangePasswordInputData changePasswordInputData) {
        // Precondition from #22: the user is logged in
        Optional<User> user = currentUserReader.currentUser();
        if (user.isEmpty()) {
            userPresenter.prepareFailView("You need to be logged in to change your password.");
            return;
        }

        String currentPassword = changePasswordInputData.getCurrentPassword();
        String newPassword = changePasswordInputData.getNewPassword();

        // Catch what we can here first, so we don't bother the account service for nothing
        String problem = findProblem(currentPassword, newPassword, changePasswordInputData.getConfirmPassword());
        if (problem != null) {
            userPresenter.prepareFailView(problem);
            return;
        }

        ChangePasswordUserDataAccessInterface.Result result =
                userDataAccessObject.changePassword(currentPassword, newPassword);

        if (result == ChangePasswordUserDataAccessInterface.Result.CHANGED) {
            userPresenter.prepareSuccessView(new ChangePasswordOutputData(user.get().getUsername()));

        } else if (result == ChangePasswordUserDataAccessInterface.Result.WRONG_CURRENT_PASSWORD) {
            userPresenter.prepareFailView("Current password is incorrect.");

        } else if (result == ChangePasswordUserDataAccessInterface.Result.REJECTED) {
            userPresenter.prepareFailView("That new password wasn't accepted. Try a stronger one.");

        } else {
            userPresenter.prepareFailView("Couldn't change your password right now. Please try again.");
        }
    }

    /** Returns what's wrong with the typed passwords, or null if they're fine. */
    private String findProblem(String currentPassword, String newPassword, String confirmPassword) {
        if (currentPassword == null || currentPassword.isEmpty()) {
            return "Password inputted is empty.";
        }

        // Supabase needs at least 8, so catch it here with a clearer message
        if (!AccountPassword.isLongEnough(newPassword)) {
            return "New password must be at least " + AccountPassword.MIN_LENGTH + " characters.";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "New passwords don't match.";
        }

        if (newPassword.equals(currentPassword)) {
            return "New password must be different from your current one.";
        }

        return null;
    }
}

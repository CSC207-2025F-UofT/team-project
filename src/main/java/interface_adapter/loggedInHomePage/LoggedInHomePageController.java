package interface_adapter.loggedInHomePage;

import entity.user.User;
import use_case.changePassword.ChangePasswordInputBoundary;
import use_case.changePassword.ChangePasswordInputData;
import use_case.deleteAccount.DeleteAccountInputBoundary;
import use_case.deleteAccount.DeleteAccountInputData;
import use_case.deleteAccount.DeleteAccountInteractor;
import use_case.loggedInHomePage.LoggedInHomePageInputBoundary;

/**
 * Controller for the Change Password Use Case.
 */
public class LoggedInHomePageController {
    private final LoggedInHomePageInputBoundary loggedInHomePageInputBoundary;
    private final ChangePasswordInputBoundary userChangePasswordUseCaseInteractor;
    private final DeleteAccountInputBoundary deleteAccountInputBoundary;

    public LoggedInHomePageController(LoggedInHomePageInputBoundary loggedInHomePageInputBoundary,
                                      ChangePasswordInputBoundary userChangePasswordUseCaseInteractor,
                                      DeleteAccountInputBoundary deleteAccountInputBoundary) {
        this.loggedInHomePageInputBoundary = loggedInHomePageInputBoundary;
        this.userChangePasswordUseCaseInteractor = userChangePasswordUseCaseInteractor;
        this.deleteAccountInputBoundary = deleteAccountInputBoundary;
    }

    public void switchToLoggedInSearchView() {
        loggedInHomePageInputBoundary.switchToLoggedInSearchView();
    }

    public void switchToLoggedInFavouritesView() {
        loggedInHomePageInputBoundary.switchToLoggedInFavouritesView();
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

    public void deleteAccount(String currentUserName) {
        final DeleteAccountInputData deleteAccountInputData = new DeleteAccountInputData(currentUserName);
        deleteAccountInputBoundary.execute(deleteAccountInputData);
    }

    public void switchToSettings(String username) {
        loggedInHomePageInputBoundary.switchToSettings(username);
    }
}

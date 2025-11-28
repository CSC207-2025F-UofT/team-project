package interface_adapter.loggedInHomePage;

import entity.user.User;
import use_case.changePassword.ChangePasswordInputBoundary;
import use_case.changePassword.ChangePasswordInputData;
import use_case.loggedInHomePage.LoggedInHomePageInputBoundary;

/**
 * Controller for the Change Password Use Case.
 */
public class LoggedInHomePageController {
    private final LoggedInHomePageInputBoundary loggedInHomePageInputBoundary;
    private final ChangePasswordInputBoundary userChangePasswordUseCaseInteractor;

    public LoggedInHomePageController(LoggedInHomePageInputBoundary loggedInHomePageInputBoundary,
                                      ChangePasswordInputBoundary userChangePasswordUseCaseInteractor) {
        this.loggedInHomePageInputBoundary = loggedInHomePageInputBoundary;
        this.userChangePasswordUseCaseInteractor = userChangePasswordUseCaseInteractor;
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
}

package interface_adapter.user_logout;

import use_case.user_logout.UserLogoutInputBoundary;

/**
 * The controller for the Logout Use Case.
 */
public class UserLogoutController {

    private UserLogoutInputBoundary logoutUseCaseInteractor;

    public UserLogoutController(UserLogoutInputBoundary logoutUseCaseInteractor) {
        // Save the interactor in the instance variable.
        this.logoutUseCaseInteractor = logoutUseCaseInteractor;
    }

    /**
     * Executes the Logout Use Case.
     */
    public void execute() {
        // run the use case interactor for the logout use case
        logoutUseCaseInteractor.execute();
    }
}

package interface_adapter.settings.logout;

import use_case.logout.LogoutInputBoundary;

/**
 * The controller for the Logout Use Case.
 */
public class SettingsLogoutController {

    private LogoutInputBoundary logoutUseCaseInteractor;

    public SettingsLogoutController(LogoutInputBoundary logoutUseCaseInteractor) {
        this.logoutUseCaseInteractor = logoutUseCaseInteractor;
    }

    /**
     * Executes the Logout Use Case.
     */
    public void execute() {
        logoutUseCaseInteractor.execute();
    }
}
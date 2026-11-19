package interface_adapter.logout;

import use_case.logout.LogoutInputBoundary;

/**
 * Triggers the Logout use case (from the Log out button).
 */
public class LogoutController {
    private final LogoutInputBoundary logoutInteractor;

    public LogoutController(LogoutInputBoundary logoutInteractor) {
        this.logoutInteractor = logoutInteractor;
    }

    public void execute() {
        logoutInteractor.execute();
    }
}

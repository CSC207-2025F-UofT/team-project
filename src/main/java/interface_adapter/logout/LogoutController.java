package interface_adapter.logout;

import use_case.logout.LogoutInputBoundary;

/**
 * Controller for the Logout use case.
 */
public class LogoutController {

    private final LogoutInputBoundary logoutInteractor;

    public LogoutController(LogoutInputBoundary logoutInteractor) {
        this.logoutInteractor = logoutInteractor;
    }

    /**
     * Executes the Logout use case.
     */
    public void execute() {
        logoutInteractor.execute();
    }
}

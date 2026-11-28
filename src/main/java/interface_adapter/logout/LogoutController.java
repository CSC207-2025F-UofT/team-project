package interface_adapter.logout;

import use_case.logout.LogoutInputBoundary;

/**
 * The controller for the Log Out Use Case.
 */
public class LogoutController {

    private final LogoutInputBoundary logoutUseCaseInteractor;

    /**
     * Creates the controller.
     * @param logoutUseCaseInteractor the interactor that logs the user out
     */
    public LogoutController(LogoutInputBoundary logoutUseCaseInteractor) {
        this.logoutUseCaseInteractor = logoutUseCaseInteractor;
    }

    /**
     * Executes the Log Out Use Case.
     */
    public void execute() {
        logoutUseCaseInteractor.execute();
    }
}

package interface_adapter.Logout;
import user_case.Logout.LogoutInputBoundary;

public class LogoutController {

    private LogoutInputBoundary logoutUseCaseInteractor;

    public LogoutController(LogoutInputBoundary logoutUseCaseInteractor) {
        this.logoutUseCaseInteractor = logoutUseCaseInteractor;
    }
    public void execute() {
        logoutUseCaseInteractor.execute();
    }
}

package use_case.logout;

/**
 * Logs out the current user.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutUserDataAccessInterface userDataAccess;
    private final LogoutOutputBoundary presenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccess, LogoutOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        String username = userDataAccess.getCurrentUsername();
        if (username == null) {
            presenter.prepareFailView("No user is logged in.");
        } else {
            userDataAccess.setCurrentUsername(null);
            presenter.prepareSuccessView(new LogoutOutputData(username));
        }
    }
}

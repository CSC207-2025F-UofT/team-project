package use_case.logout;

/**
 * The Log Out Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {

    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;

    /**
     * Creates the interactor.
     * @param userDataAccessObject records who is logged in
     * @param logoutPresenter shows the result
     */
    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessObject,
                            LogoutOutputBoundary logoutPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.logoutPresenter = logoutPresenter;
    }

    @Override
    public void execute() {
        final String currentUserId = userDataAccessObject.getCurrentUserId();
        final boolean wasLoggedIn = currentUserId != null && !currentUserId.isBlank();
        userDataAccessObject.setCurrentUserId(null);
        logoutPresenter.prepareSuccessView(new LogoutOutputData(wasLoggedIn));
    }
}

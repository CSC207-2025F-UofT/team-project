package use_case.user_logout;

/**
 * The Logout Interactor.
 */
public class UserLogoutInteractor implements UserLogoutInputBoundary {
    private UserLogoutUserDataAccessInterface userDataAccessObject;
    private UserLogoutOutputBoundary logoutPresenter;

    public UserLogoutInteractor(UserLogoutUserDataAccessInterface userDataAccessInterface,
                            UserLogoutOutputBoundary userLogoutOutputBoundary) {
        userDataAccessObject = userDataAccessInterface;
        logoutPresenter = userLogoutOutputBoundary;
    }

    @Override
    public void execute() {
        // * set the current username to null in the DAO
        // * instantiate the `UserLogoutOutputData`, which needs to contain the username.
        // * tell the presenter to prepare a success view.
        UserLogoutOutputData userLogoutOutputData = new UserLogoutOutputData(userDataAccessObject.getCurrentUsername());
        userDataAccessObject.setCurrentUsername(null);
        logoutPresenter.prepareSuccessView(userLogoutOutputData);
    }
}


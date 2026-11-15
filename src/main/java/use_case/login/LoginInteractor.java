package use_case.login;

import entity.User;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    private static final String LOGIN_ERROR = "No account with the entered username and password.";

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(LOGIN_ERROR);
        }
        else {
            final User user = userDataAccessObject.get(username);
            if (!password.equals(user.getPassword())) {
                loginPresenter.prepareFailView(LOGIN_ERROR);
            }
            else {
                loginPresenter.prepareSuccessView(new LoginOutputData(user.getName()));
            }
        }
    }
}

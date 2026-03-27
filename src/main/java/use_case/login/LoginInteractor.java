package use_case.login;

import entity.User;
import entity.UserFactory;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        // Validate user exists
        if (!userDataAccessObject.existsByUsername(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
            return;
        }

        // Get user and authenticate
        final User user = userDataAccessObject.get(username);
        final String inputPasswordHash = UserFactory.hashPasswordSHA256(password);

        if (!user.authenticatePassword(inputPasswordHash)) {
            loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
        } else {
            userDataAccessObject.setCurrentUsername(username);
            final LoginOutputData loginOutputData = new LoginOutputData(user.getUsername());
            loginPresenter.prepareSuccessView(loginOutputData);
        }
    }

    @Override
    public void switchToSignupView() {
        // Delegate view switching to the presenter
        loginPresenter.switchToSignupView();
    }
}

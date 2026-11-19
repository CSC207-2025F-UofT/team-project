package use_case.login;

import entity.User;

/**
 * Logs a user in if the account exists and the password is correct.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccess;
    private final LoginOutputBoundary presenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccess, LoginOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData input) {
        String username = input.getUsername();
        String password = input.getPassword();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            presenter.prepareFailView("Username and password can't be empty.");
        } else if (!userDataAccess.existsByName(username)) {
            presenter.prepareFailView(username + ": Account does not exist.");
        } else {
            User user = userDataAccess.get(username);
            if (!user.getPassword().equals(password)) {
                presenter.prepareFailView("Incorrect password for \"" + username + "\".");
            } else {
                userDataAccess.setCurrentUsername(username);
                presenter.prepareSuccessView(new LoginOutputData(username));
            }
        }
    }

    @Override
    public void switchToSignupView() {
        presenter.switchToSignupView();
    }
}

package org.example.cocktailcraft.use_case.login;

public class LoginInteractor implements LoginInputBoundary {

    private final LoginDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(
            LoginDataAccessInterface userDataAccessObject,
            LoginOutputBoundary loginPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        String username = loginInputData.getUsername();
        String password = loginInputData.getPassword();

        if (username == null || username.isBlank()) {
            loginPresenter.prepareFailView("Username cannot be empty.");
        } else if (password == null || password.isBlank()) {
            loginPresenter.prepareFailView("Password cannot be empty.");
        } else if (!userDataAccessObject.validateCredentials(username, password)) {
            loginPresenter.prepareFailView("Invalid username or password.");
        } else {
            LoginOutputData outputData = new LoginOutputData(username);
            loginPresenter.prepareSuccessView(outputData);
        }
    }
}


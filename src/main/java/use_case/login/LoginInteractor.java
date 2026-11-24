package use_case.login;

import entities.RegularUser;
import entities.Researcher;
import entities.User;
import use_case.login.LoginInputData;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import use_case.login.LoginUserDataAccessInterface;

public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface dao;
    private final LoginOutputBoundary presenter;

    public LoginInteractor(LoginUserDataAccessInterface dao, LoginOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        final String username = inputData.getUsername() == null ? "" : inputData.getUsername().trim(); // If the username is null, use an empty string "". Otherwise, take the username and strip any leading and trailing whitespace.

        if (username.isEmpty()) {
            presenter.prepareFailView("Username cannot be empty.");
        }
        else if (! dao.existsByName(username)) {
            presenter.prepareFailView("Username doesn't exists. Please Sign up.");
        }
        else {
            final User user = dao.get(username);
            presenter.prepareSuccessView(new LoginOutputData(username, user.getRole()));
        }
    }
    @Override
    public void switchToSignupView() {
        presenter.switchToSignupView();
    }
}

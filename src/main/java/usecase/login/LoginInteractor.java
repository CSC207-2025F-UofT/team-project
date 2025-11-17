package usecase.login;

import entity.User;

public class LoginInteractor implements LoginInputBoundary {

    public LoginUserDataAccessInterface loginUserDataAccessObject;
    public LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface loginUserDataAccessInterface,
                           LoginOutputBoundary loginPresenter) {
        this.loginUserDataAccessObject = loginUserDataAccessInterface;
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();
        if (!loginUserDataAccessObject.usernameExists(username)) {
            loginPresenter.loginFailureView(username + ": This account does not exist!");
        }
        else {
            final String pwd = loginUserDataAccessObject.getUser(username).getPassword();
            if (!password.equals(pwd)) {
                loginPresenter.loginFailureView("Incorrect password for \"" + username + "\".");
            }
            else {
                final User user = loginUserDataAccessObject.getUser(loginInputData.getUsername());
                loginUserDataAccessObject.setCurrentUsername(username);
                final LoginOutputData loginOutputData = new LoginOutputData(user.getName());
                loginPresenter.loginSuccessView(loginOutputData);
            }
        }
    }
}


package use_case.login;

import entity.User;

public class LoginInteractor implements LoginInputBoundary {

    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(
            LoginUserDataAccessInterface userDataAccessObject,
            LoginOutputBoundary loginPresenter) {

        this.userDataAccessObject = userDataAccessObject;
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void execute(LoginInputData loginInputData) {

        String username = loginInputData.getUsername();
        String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView("User does not exist.");
            return;
        }

        User user = userDataAccessObject.get(username);

        if (!user.getPassword().equals(password)) {
            loginPresenter.prepareFailView("Incorrect password.");
            return;
        }

        userDataAccessObject.setCurrentUsername(username);

        LoginOutputData loginOutputData =
                new LoginOutputData(username);

        loginPresenter.prepareSuccessView(loginOutputData);
    }
}

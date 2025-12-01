package interface_adapter.user_login;

import use_case.user_login.UserLoginInputBoundary;
import use_case.user_login.UserLoginInputData;

public class UserLoginController {

    private final UserLoginInputBoundary loginUseCaseInteractor;

    public UserLoginController(UserLoginInputBoundary loginUseCaseInteractor) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
    }

    /**
     * Executes the Login Use Case.
     * @param username the username of the user logging in
     * @param password the password of the user logging in
     */
    public void execute(String username, String password) {
        final UserLoginInputData loginInputData = new UserLoginInputData(
                username, password);

        loginUseCaseInteractor.execute(loginInputData);
    }
}

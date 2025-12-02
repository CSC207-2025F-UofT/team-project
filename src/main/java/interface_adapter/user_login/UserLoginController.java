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
     * @param userType the userType of the user logging in
     * @param username the username of the user logging in
     * @param password the password of the user logging in
     */
    public void execute(String userType, String username, String password) {
        final UserLoginInputData loginInputData = new UserLoginInputData(
                userType, username, password);

        loginUseCaseInteractor.execute(loginInputData);
    }

    /**
     * Executes the Login Use Case.
     */
    public void goBack() {
        loginUseCaseInteractor.goBack();
	}
}

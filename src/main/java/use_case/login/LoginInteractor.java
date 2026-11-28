package use_case.login;

import entity.User;
import use_case.PasswordHasher;

/**
 * The Login Interactor.
 * Logs in adopters and admins with email + password.
 */
public class LoginInteractor implements LoginInputBoundary {
    private static final String INVALID_CREDENTIALS = "Invalid email or password.";

    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String email = loginInputData.getEmail();
        final String password = loginInputData.getPassword();

        if (isEmpty(email)) {
            loginPresenter.prepareFailView("Email cannot be empty");
            return;
        }
        if (isEmpty(password)) {
            loginPresenter.prepareFailView("Password cannot be empty");
            return;
        }
        // Same message for unknown email and wrong password,
        // so login cannot be used to probe which emails are registered.
        final User user = userDataAccessObject.getByEmail(email);
        if (user == null || !PasswordHasher.hash(password).equals(user.getPasswordHash())) {
            loginPresenter.prepareFailView(INVALID_CREDENTIALS);
            return;
        }

        userDataAccessObject.setCurrentUserId(user.getId());

        final LoginOutputData loginOutputData =
                new LoginOutputData(user.getName(), user.getId(), user.getRole());
        loginPresenter.prepareSuccessView(loginOutputData);
    }


    private static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
}

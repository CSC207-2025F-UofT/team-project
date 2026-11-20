package use_case.login;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Entity.User;

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
        if (user == null || !hashPassword(password).equals(user.getPasswordHash())) {
            loginPresenter.prepareFailView(INVALID_CREDENTIALS);
            return;
        }

        userDataAccessObject.setCurrentUserId(user.getId());

        final LoginOutputData loginOutputData =
                new LoginOutputData(user.getName(), user.getId(), user.getRole());
        loginPresenter.prepareSuccessView(loginOutputData);
    }

    /**
     * Hashes the raw password with SHA-256 and returns it as a lowercase hex string.
     * Must stay identical to the hashing in SignupInteractor and AdminSignupInteractor.
     */
    private static String hashPassword(String rawPassword) {
        try {
            final byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, hash));
        }
        catch (NoSuchAlgorithmException ex) {
            // Every Java platform is required to support SHA-256.
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }

    private static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
}

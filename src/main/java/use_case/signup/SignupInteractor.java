package use_case.signup;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import Entity.User;
import Entity.User.Role;
import Entity.UserFactory;

/**
 * The Signup Interactor for adopters (regular users).
 * Admin accounts are created through the admin_signup use case instead.
 */
public class SignupInteractor implements SignupInputBoundary {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        final String error = validate(signupInputData);
        if (error != null) {
            userPresenter.prepareFailView(error);
            return;
        }

        final User user = userFactory.create(signupInputData.getUsername(),
                                             signupInputData.getId(),
                                             signupInputData.getEmail(),
                                             hashPassword(signupInputData.getPassword()),
                                             Role.ADOPTER,
                                             signupInputData.getPreference());
        userDataAccessObject.save(user);

        final SignupOutputData signupOutputData = new SignupOutputData(user.getName());
        userPresenter.prepareSuccessView(signupOutputData);
    }

    /**
     * Returns the first validation error, or null if the input is valid.
     */
    private String validate(SignupInputData input) {
        if (isEmpty(input.getUsername())) {
            return "Username cannot be empty";
        }
        if (isEmpty(input.getId())) {
            return "Id cannot be empty";
        }
        if (isEmpty(input.getEmail())) {
            return "Email cannot be empty";
        }
        if (!EMAIL_PATTERN.matcher(input.getEmail()).matches()) {
            return "Invalid email format";
        }
        if (isEmpty(input.getPassword())) {
            return "New password cannot be empty";
        }
        if (input.getPassword().length() < MIN_PASSWORD_LENGTH) {
            return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
        }
        if (!input.getPassword().equals(input.getRepeatPassword())) {
            return "Passwords don't match.";
        }
        if (isEmpty(input.getPreference())) {
            return "Preference cannot be empty";
        }
        if (userDataAccessObject.existsById(input.getId())) {
            return "User with this id already exists.";
        }
        if (userDataAccessObject.existsByEmail(input.getEmail())) {
            return "Email is already registered.";
        }
        return null;
    }

    /**
     * Hashes the raw password with SHA-256 and returns it as a lowercase hex string.
     * Login must use the same algorithm to verify passwords.
     */
    private static String hashPassword(String rawPassword) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        }
        catch (NoSuchAlgorithmException ex) {
            // Every Java platform is required to support SHA-256.
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }

    private static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}

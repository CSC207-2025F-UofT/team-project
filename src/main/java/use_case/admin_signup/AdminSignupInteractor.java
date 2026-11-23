package use_case.admin_signup;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import entity.User;
import entity.User.Role;
import entity.UserFactory;

/**
 * The Admin Signup Interactor.
 * Creates an admin account for anyone with a valid admin invite code.
 */
public class AdminSignupInteractor implements AdminSignupInputBoundary {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final AdminSignupUserDataAccessInterface userDataAccessObject;
    private final AdminSignupOutputBoundary adminSignupPresenter;
    private final UserFactory userFactory;
    private final String expectedInviteCode;

    /**
     * Creates the interactor.
     * @param userDataAccessObject where users are checked and saved
     * @param adminSignupPresenter the presenter for the result
     * @param userFactory creates the new admin user
     * @param expectedInviteCode the valid admin invite code; if null or empty, every code is rejected,
     *                           so nobody can sign up as an admin
     */
    public AdminSignupInteractor(AdminSignupUserDataAccessInterface userDataAccessObject,
                                 AdminSignupOutputBoundary adminSignupPresenter,
                                 UserFactory userFactory,
                                 String expectedInviteCode) {
        this.userDataAccessObject = userDataAccessObject;
        this.adminSignupPresenter = adminSignupPresenter;
        this.userFactory = userFactory;
        this.expectedInviteCode = expectedInviteCode;
    }

    @Override
    public void execute(AdminSignupInputData adminSignupInputData) {
        final String error = validate(adminSignupInputData);
        if (error != null) {
            adminSignupPresenter.prepareFailView(error);
            return;
        }

        final User admin = userFactory.create(adminSignupInputData.getUsername(),
                                              adminSignupInputData.getId(),
                                              adminSignupInputData.getEmail(),
                                              hashPassword(adminSignupInputData.getPassword()),
                                              Role.ADMIN,
                                              null);
        userDataAccessObject.save(admin);

        final AdminSignupOutputData outputData = new AdminSignupOutputData(admin.getEmail());
        adminSignupPresenter.prepareSuccessView(outputData);
    }

    /**
     * Returns the first validation error, or null if the input is valid.
     */
    private String validate(AdminSignupInputData input) {
        if (!isValidInviteCode(input.getInviteCode())) {
            return "Invalid admin invite code.";
        }
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
        if (userDataAccessObject.existsById(input.getId())) {
            return "User with this id already exists.";
        }
        if (userDataAccessObject.existsByEmail(input.getEmail())) {
            return "Email is already registered.";
        }
        return null;
    }

    /**
     * Checks the entered invite code against the expected one.
     * Uses a constant-time comparison, so the code cannot be guessed from response timing.
     */
    private boolean isValidInviteCode(String code) {
        if (isEmpty(expectedInviteCode) || code == null) {
            return false;
        }
        return MessageDigest.isEqual(expectedInviteCode.getBytes(StandardCharsets.UTF_8),
                                     code.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Hashes the raw password with SHA-256 and returns it as a lowercase hex string.
     * Login must use the same algorithm to verify passwords.
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

    @Override
    public void switchToLoginView() {
        adminSignupPresenter.switchToLoginView();
    }
}

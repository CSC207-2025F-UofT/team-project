package use_case.change_password;

import entity.User;
import entity.UserFactory;
import java.util.regex.Pattern;

/**
 * The Change Password Interactor.
 */
public class ChangePasswordInteractor implements ChangePasswordInputBoundary {
    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final ChangePasswordOutputBoundary userPresenter;
    private final UserFactory userFactory;

    // Regex patterns for password validation
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile(".*\\s.*");
    private static final int MIN_PASSWORD_LENGTH = 8;

    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface changePasswordDataAccessInterface,
                                    ChangePasswordOutputBoundary changePasswordOutputBoundary,
                                    UserFactory userFactory) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.userPresenter = changePasswordOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(ChangePasswordInputData changePasswordInputData) {
        final String newPassword = changePasswordInputData.getPassword();
        final String username = changePasswordInputData.getUsername();

        if ("".equals(newPassword)) {
            userPresenter.prepareFailView("New password cannot be empty");
            return;
        }

        // Strong password check
        final String passwordError = validatePasswordStrength(newPassword);
        if (passwordError != null) {
            userPresenter.prepareFailView(passwordError);
            return;
        }

        // Retrieve the existing user to get their userId
        final User existingUser = userDataAccessObject.get(username);

        // Hash the new password
        final String newPasswordHash = UserFactory.hashPasswordSHA256(newPassword);

        // Create a new User object with the same userId but new password hash
        final User updatedUser = userFactory.loadUser(
                existingUser.getUserId(),
                existingUser.getUsername(),
                newPasswordHash
        );

        // Save the updated user
        userDataAccessObject.changePassword(updatedUser);

        // Prepare success response
        final ChangePasswordOutputData changePasswordOutputData = new ChangePasswordOutputData(updatedUser.getUsername());
        userPresenter.prepareSuccessView(changePasswordOutputData);
    }

    /**
     * Validates password strength and returns a specific error message,
     * or null if the password is strong enough.
     */
    private String validatePasswordStrength(String password) {
        StringBuilder errors = new StringBuilder();

        if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.append("Password must be at least ")
                    .append(MIN_PASSWORD_LENGTH)
                    .append(" characters long.\n");
        }
        if (WHITESPACE_PATTERN.matcher(password).find()) {
            errors.append("Password must not contain spaces.\n");
        }
        if (!LOWERCASE_PATTERN.matcher(password).find()) {
            errors.append("Password must contain at least one lowercase letter.\n");
        }
        if (!UPPERCASE_PATTERN.matcher(password).find()) {
            errors.append("Password must contain at least one uppercase letter.\n");
        }
        if (!DIGIT_PATTERN.matcher(password).find()) {
            errors.append("Password must contain at least one digit.\n");
        }
        if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            errors.append("Password must contain at least one symbol (e.g. !@#$%^&*(),.?\":{}|<>).\n");
        }

        if (errors.isEmpty()) {
            return null;
        } else {
            return errors.toString().trim();
        }
    }
}

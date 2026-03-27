package use_case.signup;

import entity.User;
import entity.UserFactory;
import java.util.regex.Pattern;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    // Regex patterns for password validation
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile(".*\\s.*");
    private static final int MIN_PASSWORD_LENGTH = 8;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        final String username = signupInputData.getUsername();
        final String password = signupInputData.getPassword();
        final String repeatPassword = signupInputData.getRepeatPassword();

        if (userDataAccessObject.existsByUsername(username)) {
            userPresenter.prepareFailView("User already exists.");
        }
        else if (!password.equals(repeatPassword)) {
            userPresenter.prepareFailView("Passwords don't match.");
        }
        else if (password.isEmpty()) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else if (username.isEmpty()) {
            userPresenter.prepareFailView("Username cannot be empty");
        }
        else {
            // Check password strength with specific messages
            final String passwordError = validatePasswordStrength(password);
            if (passwordError != null) {
                userPresenter.prepareFailView(passwordError);
                return;
            }

            final String passwordHash = UserFactory.hashPasswordSHA256(password);

            final User user = userFactory.createUser(username, passwordHash);
            userDataAccessObject.save(user);

            final SignupOutputData signupOutputData = new SignupOutputData(user.getUsername());
            userPresenter.prepareSuccessView(signupOutputData);
        }
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
            // Remove trailing newline for neatness
            return errors.toString().trim();
        }
    }


    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}

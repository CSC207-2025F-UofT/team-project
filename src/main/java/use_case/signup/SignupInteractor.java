package use_case.signup;

import java.util.regex.Pattern;

import entity.User;
import entity.User.Role;
import entity.UserFactory;
import use_case.PasswordHasher;

/**
 * The Signup Interactor for adopters (regular users).
 * Admin accounts are created through the admin_signup use case instead.
 * New adopters start with no preference; they fill it in later.
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
                                             PasswordHasher.hash(signupInputData.getPassword()),
                                             Role.ADOPTER,
                                             null);
        userDataAccessObject.save(user);

        final SignupOutputData signupOutputData = new SignupOutputData(user.getEmail());
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
        if (userDataAccessObject.existsById(input.getId())) {
            return "User with this id already exists.";
        }
        if (userDataAccessObject.existsByEmail(input.getEmail())) {
            return "Email is already registered.";
        }
        return null;
    }


    private static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}

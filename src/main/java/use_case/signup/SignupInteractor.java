package use_case.signup;

import entity.User;
import entity.UserFactory;

/**
 * The Interactor for the Signup Use Case.
 *
 * Contains the business logic for user registration.
 * Validates signup data and creates new user accounts.
 *
 * Validation checks:
 * - Username must not already exist
 * - Username must not be empty
 * - Passwords must match
 * - Password must meet minimum requirements
 */
public class SignupInteractor implements SignupInputBoundary {

    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary signupPresenter;
    private final UserFactory userFactory;

    /**
     * Constructs a SignupInteractor with required dependencies.
     *
     * @param userDataAccessObject interface for checking and saving users
     * @param signupPresenter presenter interface for reporting results
     * @param userFactory user factory
     */
    public SignupInteractor(SignupUserDataAccessInterface userDataAccessObject,
                            SignupOutputBoundary signupPresenter,
                            UserFactory userFactory) {
        this.userDataAccessObject = userDataAccessObject;
        this.signupPresenter = signupPresenter;
        this.userFactory = userFactory;
    }

    /**
     * Executes the Signup Use Case.
     *
     * Validates the signup data and creates a new user if all
     * validation checks pass. Reports success or failure to signupPresenter.
     *
     * @param signupInputData contains username, password, and confirmPassword
     */
    @Override
    public void execute(SignupInputData signupInputData) {
        // Extract data
        final String username = signupInputData.getUsername();
        final String password = signupInputData.getPassword();
        final String confirmPassword = signupInputData.getConfirmPassword();

        // VALIDATION 1: Check username not empty
        if (username.isEmpty()) {
            signupPresenter.prepareFailView("Username cannot be empty.");
            return;
        }

        // VALIDATION 2: Check if user already exists
        if (userDataAccessObject.existsByName(username)) {
            signupPresenter.prepareFailView("Account \"" + username + "\" already exists.");
            return;
        }

        // VALIDATION 3: Check password length
        if (password.length() < 8) {
            signupPresenter.prepareFailView("Password must be at least 8 characters.");
            return;
        }

        // VALIDATION 4: Check passwords match
        if (!password.equals(confirmPassword)) {
            signupPresenter.prepareFailView("Passwords don't match.");
            return;
        }

        // ALL VALIDATIONS PASSED - Create and save user
        final User newUser = userFactory.create(username, password);
        userDataAccessObject.save(newUser);

        // Report success and show success view
        final SignupOutputData outputData = new SignupOutputData(username);
        signupPresenter.prepareSuccessView(outputData);
    }
}
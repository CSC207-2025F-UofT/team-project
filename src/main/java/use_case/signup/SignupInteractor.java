package use_case.signup;

import entity.User;
import entity.UserFactory;
import use_case.change_password.PasswordValidator;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
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
            final String passwordError = PasswordValidator.validate(password);
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
     * Switch to the login view.
     */
    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}

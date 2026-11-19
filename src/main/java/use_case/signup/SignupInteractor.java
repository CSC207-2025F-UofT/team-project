package use_case.signup;

import entity.User;
import entity.UserFactory;

/**
 * Creates a new account if the username is free and the passwords match.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccess;
    private final SignupOutputBoundary presenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface userDataAccess,
                            SignupOutputBoundary presenter,
                            UserFactory userFactory) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData input) {
        String username = input.getUsername();
        String password = input.getPassword();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            presenter.prepareFailView("Username and password can't be empty.");
        } else if (userDataAccess.existsByName(username)) {
            presenter.prepareFailView("User already exists.");
        } else if (!password.equals(input.getRepeatPassword())) {
            presenter.prepareFailView("Passwords don't match.");
        } else {
            User user = userFactory.create(username, password);
            userDataAccess.save(user);
            presenter.prepareSuccessView(new SignupOutputData(user.getUsername()));
        }
    }

    @Override
    public void switchToLoginView() {
        presenter.switchToLoginView();
    }
}

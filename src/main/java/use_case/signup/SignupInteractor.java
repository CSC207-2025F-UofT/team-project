package use_case.signup;

import entity.User;
import entity.UserFactory;

public class SignupInteractor implements SignupInputBoundary {

    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary signupPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(
            SignupUserDataAccessInterface userDataAccessObject,
            SignupOutputBoundary signupPresenter,
            UserFactory userFactory) {

        this.userDataAccessObject = userDataAccessObject;
        this.signupPresenter = signupPresenter;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {

        String username = signupInputData.getUsername();
        String password = signupInputData.getPassword();
        String repeatPassword = signupInputData.getRepeatPassword();

        if (username == null || username.isBlank()) {
            signupPresenter.prepareFailView(
                    "Username cannot be empty.");
            return;
        }

        if (password == null || password.isEmpty()) {
            signupPresenter.prepareFailView(
                    "Password cannot be empty.");
            return;
        }

        if (userDataAccessObject.existsByName(username)) {
            signupPresenter.prepareFailView(
                    "Username already exists.");
            return;
        }

        if (!password.equals(repeatPassword)) {
            signupPresenter.prepareFailView(
                    "Passwords do not match.");
            return;
        }

        User user = userFactory.create(username, password);

        userDataAccessObject.save(user);

        SignupOutputData signupOutputData =
                new SignupOutputData(username);

        signupPresenter.prepareSuccessView(signupOutputData);
    }
}

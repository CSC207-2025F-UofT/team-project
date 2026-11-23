package org.example.cocktailcraft.use_case.signup;

public class SignupInteractor implements SignupInputBoundary {

    private final SignupDataAccessInterface userDataAccess;
    private final SignupOutputBoundary presenter;

    public SignupInteractor(
            SignupDataAccessInterface userDataAccess,
            SignupOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SignupInputData inputData) {
        String username = inputData.getUsername();
        String password = inputData.getPassword();
        String confirmation = inputData.getConfirmation();

        if (username == null || username.isBlank()
                || password == null || password.isBlank()
                || confirmation == null || confirmation.isBlank()) {
            presenter.prepareFailView("All fields are required.");
            return;
        }

        if (!password.equals(confirmation)) {
            presenter.prepareFailView("Passwords do not match.");
            return;
        }

        if (userDataAccess.existsByName(username)) {
            presenter.prepareFailView("Username already exists.");
            return;
        }

        userDataAccess.createUser(username, password);

        SignupOutputData outputData = new SignupOutputData(username);
        presenter.prepareSuccessView(outputData);
    }
}

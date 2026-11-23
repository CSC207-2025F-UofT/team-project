package use_case.sign_up;

import entity.User;

public class SignupInteractor implements SignupInputBoundary{
    private final SignupUserDataAccessInterface userDataAccess;
    private final SignupOutputBoundary presenter;

    public SignupInteractor(SignupUserDataAccessInterface userDataAccess,
                            SignupOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SignupInputData inputData) {
        if (inputData.getUsername() == null || inputData.getUsername().isBlank()) {
            presenter.prepareFailView("Please input a valid username.");
            return;
        } else if (inputData.getPassword() == null || inputData.getPassword().isBlank()) {
            presenter.prepareFailView("Please input a valid password.");
            return;
        }
    }

    // still needs to check if username already exists
}

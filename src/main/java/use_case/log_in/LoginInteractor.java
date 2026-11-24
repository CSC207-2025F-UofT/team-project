package use_case.log_in;
import entity.User;

public class LoginInteractor implements LoginInputBoundary{
    private final LoginUserDataAccessInterface userDataAccess;
    private final LoginOutputBoundary presenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccess,
                           LoginOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        String username = inputData.getUsername();
        String password = inputData.getPassword();

        if (inputData.getUsername() == null || inputData.getUsername().isBlank()) {
            presenter.prepareFailView("Please input a valid username.");
            return;
        } else if (inputData.getPassword() == null || inputData.getPassword().isBlank()) {
            presenter.prepareFailView("Please input a valid password.");
            return;
        } else if (!userDataAccess.existsByName(inputData.getUsername())) {
            presenter.prepareFailView("Username does not exists! Please Sign up.");
            return;
        }

        User account = userDataAccess.get(inputData.getUsername());
        if (!account.getPassword().equals(password)) {
            presenter.prepareFailView("Incorrect password. Please try again.");
            return;
        }

        userDataAccess.setCurrentUsername(username);
        presenter.prepareSuccessView(new LoginOutputData(username));
    }
}

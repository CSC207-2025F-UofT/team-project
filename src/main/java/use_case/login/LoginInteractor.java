package use_case.login;
import entity.UserProfile;

public class LoginInteractor implements LoginInputBoundary{
    private final LoginDataAccessInterface dataAccessObject;
    private final LoginOutputBoudary presenter;

    public LoginInteractor(LoginDataAccessInterface dataAccessObject,
            LoginOutputBoudary presenter) {

        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {

        String userName = inputData.getUserName();
        String password = inputData.getPassword();

        if (userName == null || userName.isBlank() || password == null || password.isBlank()) {

            presenter.prepareFailureView("Username and password are required.");
            return;
        }

        try {
            UserProfile user = dataAccessObject.findUserByUsername(userName.trim());

            if (user == null) {
                presenter.prepareFailureView("Incorrect username or password.");

            } else if (!dataAccessObject.passwordMatches(user, password)) {

                presenter.prepareFailureView("Incorrect username or password.");

            } else {
                LoginOutputData outputData = new LoginOutputData(user.getId(), user.getName());

                presenter.prepareSuccessView(outputData);
            }

        } catch (IllegalStateException e) {
            presenter.prepareFailureView("Unable to log in.");
        }
    }
}

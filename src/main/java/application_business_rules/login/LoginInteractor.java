package application_business_rules.login;

import entity.UserProfile;

/** Authenticates a user through the use-case-owned Data Access Interface. */
public final class LoginInteractor implements LoginInputBoundary {
    private final LoginDataAccessInterface dataAccess;
    private final LoginOutputBoundary presenter;

    public LoginInteractor(LoginDataAccessInterface dataAccess, LoginOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        if (inputData.getUsername() == null || inputData.getUsername().isBlank()
                || inputData.getPassword() == null || inputData.getPassword().isBlank()) {
            presenter.prepareFailView("Enter your username and password.");
            return;
        }
        UserProfile profile = dataAccess.findUserByUsername(inputData.getUsername().trim());
        if (profile == null || !dataAccess.passwordMatches(profile, inputData.getPassword())) {
            presenter.prepareFailView("Incorrect username or password.");
            return;
        }
        presenter.prepareSuccessView(new LoginOutputData(profile.getId(), profile.getName()));
    }
}

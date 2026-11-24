package use_case.admin_login;

import use_case.PasswordHasher;

/**
 * Logs the admin in if the password matches the stored admin password.
 */
public class AdminLoginInteractor implements AdminLoginInputBoundary {
    private final AdminLoginDataAccessInterface dataAccess;
    private final AdminLoginOutputBoundary presenter;
    private final PasswordHasher passwordHasher;

    public AdminLoginInteractor(AdminLoginDataAccessInterface dataAccess,
                                AdminLoginOutputBoundary presenter,
                                PasswordHasher passwordHasher) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void execute(AdminLoginInputData inputData) {
        final String password = inputData.getPassword() == null ? "" : inputData.getPassword();

        if (password.isEmpty()) {
            presenter.prepareFailView("Please enter the admin password.");
        }
        else if (passwordHasher.matches(password, dataAccess.getAdmin().getPasswordHash())) {
            presenter.prepareSuccessView();
        }
        else {
            presenter.prepareFailView("Incorrect admin password.");
        }
    }
}

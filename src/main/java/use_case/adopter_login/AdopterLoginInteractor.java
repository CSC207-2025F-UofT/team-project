package use_case.adopter_login;

import entity.Adopter;
import use_case.PasswordHasher;

/**
 * Logs an adopter in if the username exists and the password matches.
 */
public class AdopterLoginInteractor implements AdopterLoginInputBoundary {
    private final AdopterLoginDataAccessInterface dataAccess;
    private final AdopterLoginOutputBoundary presenter;
    private final PasswordHasher passwordHasher;

    public AdopterLoginInteractor(AdopterLoginDataAccessInterface dataAccess,
                                  AdopterLoginOutputBoundary presenter,
                                  PasswordHasher passwordHasher) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void execute(AdopterLoginInputData inputData) {
        final String username = inputData.getUsername() == null ? "" : inputData.getUsername().trim();
        final String password = inputData.getPassword() == null ? "" : inputData.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            presenter.prepareFailView("Please enter username and password.");
        }
        else if (!dataAccess.existsByUsername(username)) {
            presenter.prepareFailView("Account \"" + username + "\" does not exist.");
        }
        else {
            final Adopter adopter = dataAccess.get(username);
            if (passwordHasher.matches(password, adopter.getPasswordHash())) {
                presenter.prepareSuccessView(new AdopterLoginOutputData(adopter.getUsername()));
            }
            else {
                presenter.prepareFailView("Incorrect password.");
            }
        }
    }
}

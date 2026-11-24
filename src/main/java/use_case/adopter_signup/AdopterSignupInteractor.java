package use_case.adopter_signup;

import entity.Adopter;
import use_case.PasswordHasher;

/**
 * Creates a new adopter account after validating the input.
 */
public class AdopterSignupInteractor implements AdopterSignupInputBoundary {
    public static final int MIN_PASSWORD_LENGTH = 6;
    private static final String USERNAME_PATTERN = "[A-Za-z0-9_]{3,20}";

    private final AdopterSignupDataAccessInterface dataAccess;
    private final AdopterSignupOutputBoundary presenter;
    private final PasswordHasher passwordHasher;

    public AdopterSignupInteractor(AdopterSignupDataAccessInterface dataAccess,
                                   AdopterSignupOutputBoundary presenter,
                                   PasswordHasher passwordHasher) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void execute(AdopterSignupInputData inputData) {
        final String username = nullToEmpty(inputData.getUsername()).trim();
        final String password = nullToEmpty(inputData.getPassword());
        final String repeatPassword = nullToEmpty(inputData.getRepeatPassword());

        if (username.isEmpty() || password.isEmpty()) {
            presenter.prepareFailView("Please fill in username and password.");
        }
        else if (!username.matches(USERNAME_PATTERN)) {
            presenter.prepareFailView("Username must be 3-20 characters: letters, digits or _.");
        }
        else if (dataAccess.existsByUsername(username)) {
            presenter.prepareFailView("Username \"" + username + "\" is already taken.");
        }
        else if (password.length() < MIN_PASSWORD_LENGTH) {
            presenter.prepareFailView("Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        else if (!password.equals(repeatPassword)) {
            presenter.prepareFailView("Passwords don't match.");
        }
        else {
            final Adopter adopter = new Adopter(username, passwordHasher.hash(password));
            dataAccess.save(adopter);
            presenter.prepareSuccessView(new AdopterSignupOutputData(username));
        }
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}

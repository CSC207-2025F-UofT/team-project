package use_case.signup;

import entity.PlayerDataFactory;

/**
 * The Sign Up Interactor: checks the new account's details and saves it.
 */
public class SignupInteractor implements SignupInputBoundary {

    private final SignupDataAccessInterface dataAccess;
    private final SignupOutputBoundary presenter;
    private final PlayerDataFactory playerDataFactory;

    /**
     * Creates the interactor.
     *
     * @param dataAccess where player accounts are stored
     * @param presenter receives the result
     * @param playerDataFactory creates new player accounts
     */
    public SignupInteractor(SignupDataAccessInterface dataAccess,
                            SignupOutputBoundary presenter,
                            PlayerDataFactory playerDataFactory) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.playerDataFactory = playerDataFactory;
    }

    @Override
    public void execute(SignupInputData inputData) {
        final String username = inputData.getUsername();
        final String password = inputData.getPassword();

        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Please choose a username.");
            return;
        }
        if (!username.equals(username.strip())) {
            presenter.prepareFailView("Username can't start or end with a space.");
            return;
        }
        if (password == null || password.isBlank()) {
            presenter.prepareFailView("Please choose a password.");
            return;
        }
        if (!password.equals(inputData.getRepeatPassword())) {
            presenter.prepareFailView("Passwords don't match.");
            return;
        }
        if (dataAccess.existsById(username)) {
            presenter.prepareFailView("The username \"" + username + "\" is already taken.");
            return;
        }

        dataAccess.save(playerDataFactory.create(username, password));
        presenter.prepareSuccessView(new SignupOutputData(username));
    }

    @Override
    public void switchToLoginView() {
        presenter.switchToLoginView();
    }
}
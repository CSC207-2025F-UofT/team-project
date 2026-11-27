package use_case.login;

import entity.playerData;

/**
 * The Login Interactor: checks the username and password against the stored account.
 */
public class LoginInteractor implements LoginInputBoundary {

    private final LoginDataAccessInterface dataAccess;
    private final LoginOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess where player accounts are stored
     * @param presenter receives the result
     */
    public LoginInteractor(LoginDataAccessInterface dataAccess, LoginOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        final String username = inputData.getUsername();
        final String password = inputData.getPassword();

        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Please enter your username.");
            return;
        }
        if (password == null || password.isEmpty()) {
            presenter.prepareFailView("Please enter your password.");
            return;
        }
        if (!dataAccess.existsById(username)) {
            presenter.prepareFailView("No account found for \"" + username + "\".");
            return;
        }

        final playerData player = dataAccess.getPlayerData(username);
        if (!player.getPassword().equals(password)) {
            presenter.prepareFailView("Incorrect password.");
            return;
        }

        dataAccess.setCurrentPlayerId(player.getId());
        presenter.prepareSuccessView(new LoginOutputData(player.getId()));
    }

    @Override
    public void switchToSignupView() {
        presenter.switchToSignupView();
    }
}

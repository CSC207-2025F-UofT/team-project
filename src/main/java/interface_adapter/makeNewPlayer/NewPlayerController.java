package interface_adapter.makeNewPlayer;

import use_case.makeNewPlayer.NewPlayerInputBoundary;
import use_case.makeNewPlayer.NewPlayerInputData;

/**
 * Controller for making a new player
 */
public class NewPlayerController {
    private final NewPlayerInputBoundary newPlayerUseCaseInteractor;

    public NewPlayerController(NewPlayerInputBoundary newPlayerUseCaseInteractor) {
        this.newPlayerUseCaseInteractor = newPlayerUseCaseInteractor;
    }

    /**
     * Executes making a new player.
     * @param username the username to sign up
     */
    public void execute(String username) {
        final NewPlayerInputData newPlayerInputData = new NewPlayerInputData(
                username);

        newPlayerUseCaseInteractor.execute(newPlayerInputData);
    }

    /**
     * Executes the "switch to WelcomeView" Use Case.
     */
    public void switchToWelcomeView() {
        newPlayerUseCaseInteractor.switchToWelcomeView();
    }

}

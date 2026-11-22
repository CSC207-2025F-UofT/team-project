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
    public void execute(String username, String password1, String password2) {
        final NewPlayerInputData signupInputData = new NewPlayerInputData(
                username, password1, password2);

        newPlayerUseCaseInteractor.execute(signupInputData);
    }

    /**
     * Executes the "switch to WelcomeView" Use Case.
     */
    public void switchToWelcomeView() {
        newPlayerUseCaseInteractor.switchToWelcomeView();
    }

}

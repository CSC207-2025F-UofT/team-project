package interface_adapter.welcomeMenu;

import use_case.welcomeMenu.WelcomeInputBoundary;
import use_case.welcomeMenu.WelcomeInputData;

public class WelcomeController {
    /**
     * The controller for the Welcome menu.
     */
    private final WelcomeInputBoundary welcomeUseCaseInteractor;

    public WelcomeController(WelcomeInputBoundary welcomeUseCaseInteractor) {
        this.welcomeUseCaseInteractor = welcomeUseCaseInteractor;
    }

    /**
     * Executes the Welcome menu.
     * @param username the username of the user logging in
     */
//    public void execute(String username) {
//        final WelcomeInputData welcomeInputData = new WelcomeInputData(username);
//
//        welcomeUseCaseInteractor.execute(welcomeInputData);
//
}

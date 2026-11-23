package interface_adapter.welcomeMenu;

import use_case.welcomeMenu.WelcomeInputBoundary;
import use_case.welcomeMenu.WelcomeInputData;

public class WelcomeController {
    /**
     * The controller for the Welcome menu.
     */
    public class LoginController {

        private final WelcomeInputBoundary welcomeUseCaseInteractor;

        public WelcomeController(WelcomeInputBoundary welcomeUseCaseInteractor) {
            this.welcomeUseCaseInteractor = welcomeUseCaseInteractor;
        }

        /**
         * Executes the Login Use Case.
         * @param username the username of the user logging in
         * @param password the password of the user logging in
         */
        public void execute(String username, String password) {
            final WelcomeInputData welcomeInputData = new WelcomeInputData(
                    username, password);

            welcomeUseCaseInteractor.execute(welcomeInputData);
        }
}

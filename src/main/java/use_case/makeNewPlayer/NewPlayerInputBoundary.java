package use_case.makeNewPlayer;

public interface NewPlayerInputBoundary {
    /**
     * Executes use case 1.
     * @param NewPlayerInputData the input data
     */
    void execute(NewPlayerInputData signupInputData);

    /**
     * Executes the switch to welcome view.
     */
    void switchToWelcomeView();
}

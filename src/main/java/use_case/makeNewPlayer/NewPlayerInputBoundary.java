package use_case.makeNewPlayer;

public interface NewPlayerInputBoundary {
    /**
     * Executes use case 1.
     * @param newPlayerInputData the input data
     */
    void execute(NewPlayerInputData newPlayerInputData);

    /**
     * Executes the switch to welcome view.
     */
    void switchToWelcomeView();
}

package use_case.makeNewPlayer;

public interface NewPlayerOutputBoundary {
    /**
     * Prepares the success view for the Signup Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(NewPlayerOutputData outputData);

    /**
     * Prepares the failure view for the Signup Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Welcome View.
     */
    void switchToWelcomeView();
}

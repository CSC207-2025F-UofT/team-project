package use_case.start_game;
/**
 * The output boundary for the start game use case.
 */
public interface StartGameOutputBoundary {
    /**
     * Prepares the success view for the Start Game Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(StartGameOutputData outputData);

    /**
     * Prepares the failure view for the Login Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}

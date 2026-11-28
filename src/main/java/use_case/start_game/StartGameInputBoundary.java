package use_case.start_game;
/**
 * Input Boundary Interface for the Game Start Use Case.
 */
public interface StartGameInputBoundary {
    /**
     * Executes the Start Game use case.
     * @param startGameInputData the input data
     */
    void execute(StartGameInputData startGameInputData);
}

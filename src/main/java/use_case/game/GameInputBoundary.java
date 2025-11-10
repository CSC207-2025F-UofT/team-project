package use_case.game;

/**
 * Input boundary for the Click Button use case.
 */
public interface GameInputBoundary {

    /**
     * Executes the click button use case.
     * @param gameInputData the input data
     */
    void execute(GameInputData gameInputData);
}

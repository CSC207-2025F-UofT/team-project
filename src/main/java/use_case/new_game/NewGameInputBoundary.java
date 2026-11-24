package use_case.new_game;

/**
 * Called by the NewGameController to start a new game.
 */
public interface NewGameInputBoundary {

    void execute(NewGameInputData inputData);
}

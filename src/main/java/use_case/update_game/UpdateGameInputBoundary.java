package use_case.update_game;

/** Called once per tick by the controller to advance the game. */
public interface UpdateGameInputBoundary {
    void execute(UpdateGameInputData inputData);
}

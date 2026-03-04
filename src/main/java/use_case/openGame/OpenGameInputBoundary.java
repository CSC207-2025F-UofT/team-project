package use_case.openGame;

public interface OpenGameInputBoundary {
    void execute(OpenGameInputData inputData);
    void switchToMoveScreen();
}

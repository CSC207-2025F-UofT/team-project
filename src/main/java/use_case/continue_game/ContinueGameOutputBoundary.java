package use_case.continue_game;

/**
 * Output boundary for continuing a saved game (UC5).
 */
public interface ContinueGameOutputBoundary {
    void prepareSuccessView(ContinueGameOutputData outputData);

    void prepareFailView(String message);
}

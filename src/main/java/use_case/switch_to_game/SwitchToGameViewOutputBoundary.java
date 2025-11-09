package use_case.switch_to_game;

/**
 * Output boundary for the Switch to Game View use case.
 */
public interface SwitchToGameViewOutputBoundary {

    /**
     * Prepares the view to switch to the game view.
     */
    void switchToGameView(SwitchToGameOutputData outputData);
}

package use_case.explore.move;

/**
 * Input boundary for moving between rooms (Use Case 2, steps 1-4).
 *
 * <p>The controller calls this interface instead of MoveInteractor directly,
 * so the controller doesn't depend on how the use case is implemented.</p>
 */
public interface MoveInputBoundary {

    /**
     * Moves the character in the chosen direction.
     *
     * @param inputData the chosen direction
     */
    void execute(MoveInputData inputData);

    /**
     * Presents the room the character is currently standing in without moving.
     * Used when the explore screen first opens, and by Load Game (Use Case 4, step 8).
     */
    void showCurrentRoom();
}

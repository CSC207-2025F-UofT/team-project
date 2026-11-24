package use_case.explore.move;

/**
 * Output boundary for the Move use case.
 *
 * <p>The interactor calls these methods to report what happened. The presenter
 * implements them and decides how to show it. There is one method per possible
 * outcome of the use case.</p>
 */
public interface MoveOutputBoundary {

    /**
     * Step 4: show the new room's description, items and exits.
     *
     * @param outputData the room the character is now in
     */
    void prepareSuccessView(MoveOutputData outputData);

    /**
     * Extension 2a: there is no exit in that direction.
     *
     * @param errorMessage message to show the player
     */
    void prepareFailView(String errorMessage);

    /**
     * Extension 4a: the room contains a monster, so Combat (Use Case 3) starts
     * and this use case ends.
     *
     * @param outputData the room the character is now in
     */
    void prepareCombatView(MoveOutputData outputData);
}

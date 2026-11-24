package interface_adapter.explore;

import entity.Direction;
import use_case.explore.move.MoveInputBoundary;
import use_case.explore.move.MoveInputData;

/**
 * Turns the player's direction button click into a Move request.
 *
 * <p>Controllers are thin: they only package the user's input into an input
 * data object and pass it to the use case. All the game rules are in the interactor.</p>
 */
public class MoveController {
    /** The use case to call (an interface, not the concrete interactor). */
    private final MoveInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the Move use case
     */
    public MoveController(MoveInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Called when a direction button is clicked.
     *
     * @param direction "NORTH", "SOUTH", "EAST" or "WEST"
     */
    public void execute(String direction) {
        // Turn the button's text into a Direction, e.g. "NORTH" -> Direction.NORTH,
        // and send it to the use case.
        interactor.execute(new MoveInputData(Direction.valueOf(direction)));
    }

    /** Shows the current room without moving (e.g. when the screen first opens). */
    public void showCurrentRoom() {
        interactor.showCurrentRoom();
    }
}

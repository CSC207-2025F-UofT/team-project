package interface_adapter.GameSelect;

/**
 * The controller for the Login Use Case.
 */
public class GameSelectController {

    /**
     * Executes the Login Use Case.
     * @param game is string of game wanted to be played
     * @param stakes is float of user's desired stakes
     */
    public void execute(String game, String stakes) {
        System.out.println("Play game: " + game + " at stakes: " + stakes);
    }
}

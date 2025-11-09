package interface_adapter.game;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Game View.
 */
public class GameViewModel extends ViewModel<GameState> {

    public static final String TITLE_LABEL = "Game";
    public static final String BUTTON1_LABEL = "Button 1";
    public static final String BUTTON2_LABEL = "Button 2";

    public GameViewModel() {
        super("game");
        setState(new GameState());
    }
}

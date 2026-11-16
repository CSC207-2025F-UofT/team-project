package interface_adapter.game;

/**
 * Holds the latest GameState for the view.
 * Written by the game loop thread, read by Swing's thread, so the reference is volatile.
 * GameState is immutable, so the view always sees a complete snapshot.
 */
public class GameViewModel {
    private volatile GameState state;

    public GameViewModel(GameState initialState) {
        this.state = initialState;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}

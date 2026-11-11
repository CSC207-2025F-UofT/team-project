package Game;

public class GameInteractor implements GameInputBoundary {
    private Game game;

    public GameInteractor() {
        this.game = new Game();
    }

    @Override
    public void execute(Move move) {

    }
}

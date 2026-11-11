package Game;

public class GameInteractor implements GameInputBoundary {
    private final Game game;
    private final GameOutputBoundary gamePresenter;
    public GameInteractor(GameOutputBoundary gamePresenter) {
        this.gamePresenter = gamePresenter;
        this.game = new Game();
    }

    @Override
    public void execute(Move move) {
        final int x = move.column;
        final int y = move.row;
        final int player = game.getTurn();
        final GameOutputData gameOutputData = new GameOutputData(y,x,player);
        game.playerInput(move);
        gamePresenter.prepareView(gameOutputData);


    }
}

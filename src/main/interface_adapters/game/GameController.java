package main.interface_adapters.game;

public class GameController {

    private final GamePresenter gamePresenter;

    public GameController(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
    }

    public void playerCheck() {
        gamePresenter.playerCheck();
    }

    public void gameStarted() {
        gamePresenter.gameStarted();
    }
}

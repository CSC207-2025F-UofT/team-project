package main.interface_adapters.game;

import java.util.Map;

public class GameController {

    private final GamePresenter gamePresenter; // Swap to GameInputBoundary once interactor is ready

    public GameController(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
    }

    public void gameStarted() {
        gamePresenter.gameStarted();
    }

    public void playerCheck() {
        gamePresenter.playerCheck();
    }

    public void playerCall() {
        gamePresenter.playerCall();
    }

    public void playerRaise(int amount) {
        gamePresenter.playerRaise(amount);
    }

    public void playerReraise(int amount) {
        gamePresenter.playerReraise(amount);
    }

    public void playerFold() {
        gamePresenter.playerFold();
    }
    public void playerRaise(Map<Integer, Integer> betChips, int totalAmount) {
        gamePresenter.playerRaise(betChips, totalAmount);
    }

    public void playerReraise(Map<Integer, Integer> betChips, int totalAmount) {
        gamePresenter.playerReraise(betChips, totalAmount);
    }

}
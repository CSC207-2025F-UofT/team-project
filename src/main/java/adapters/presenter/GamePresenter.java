package adapters.presenter;

import entity.Game;
import entity.GameResult;
import yinghaousecase.loadgame.LoadGameOutputBoundary;
import yinghaousecase.savegame.SaveGameOutputBoundary;
import yinghaousecase.startgame.StartGameOutputBoundary;

public class GamePresenter implements StartGameOutputBoundary,
        SaveGameOutputBoundary,
        LoadGameOutputBoundary {

    private Game currentGame;
    private GameResult lastLoadedResult;
    private Runnable onGameChanged;

    public void setOnGameChanged(Runnable callback) { this.onGameChanged = callback; }
    public Game getCurrentGame() { return currentGame; }
    public GameResult getLastLoadedResult() { return lastLoadedResult; }

    @Override
    public void presentGameStarted(Game game) {
        this.currentGame = game;
        refresh();
    }

    @Override
    public void presentSaveSuccess(String slotName) {
        System.out.println("[Presenter] Result saved to slot: " + slotName);
    }

    @Override
    public void presentSaveFailure(String slotName, String reason) {
        System.err.println("[Presenter] Save failed (" + slotName + "): " + reason);
    }

    @Override
    public void presentResultLoaded(GameResult result) {
        this.lastLoadedResult = result;
        System.out.println("[Presenter] Loaded result: "
                + result.getPlayer1Name() + " " + result.getPlayer1Score()
                + " vs "
                + result.getPlayer2Name() + " " + result.getPlayer2Score()
                + " | winner = "
                + (result.isDraw() ? "DRAW" : result.getWinnerName())
                + " | endedAt = " + result.getEndedAt());
    }

    @Override
    public void presentLoadFailure(String slotName, String reason) {
        System.err.println("[Presenter] Load failed (" + slotName + "): " + reason);
    }

    private void refresh() { if (onGameChanged != null) onGameChanged.run(); }
}
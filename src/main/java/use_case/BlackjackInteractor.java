
package use_case;

import entity.*;

public class BlackjackInteractor {
    private final BlackjackGame game;
    private final BlackjackOutputBoundary presenter;

    public BlackjackInteractor(BlackjackGame game, BlackjackOutputBoundary presenter) {
        this.game = game;
        this.presenter = presenter;
    }

    public void startRound(double bet) {
        game.startRound(bet);
        presenter.presentHands(game);
    }

    public void playerHit() {
        game.playerHit();
        presenter.presentHands(game);
    }

    public void stand() {
        game.dealerPlay();
        GameResult result = game.determineResult();
        game.updateWallet(result);
        presenter.presentResult(game, result);
    }
}
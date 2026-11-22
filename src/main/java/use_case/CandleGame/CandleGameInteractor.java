package use_case.CandleGame;

import entities.Game.CandleGame;
import entities.Game.GameCondition;
import use_case.SecretCodeGenerator.SecretCodeGenerator;

/**
 * Implements the Candle Game use case.
 */

public class CandleGameInteractor implements CandleGameInputBoundary {

    private CandleGame game;
    private final SecretCodeGenerator secretCodeGenerator;
    private final CandleGameOutputBoundary presenter;

    public CandleGameInteractor(SecretCodeGenerator secretCodeGenerator,
                                CandleGameOutputBoundary presenter) {
        this.secretCodeGenerator = secretCodeGenerator;
        this.presenter = presenter;
    }

    @Override
    public void initGame() {
        String secretCode =  secretCodeGenerator.generate();
        CandleGame game = new CandleGame(CandleGame.secretCode(), 10);

        CandleGameOutputData outputData = new CandleGameOutputData(
                game.getMaxTries(),
                game.getGameCondition());

        presenter.prepareGameView(outputData );
    }

    @Override
    public void quitGame() {
        game.setGameCondition(GameCondition.QUIT);
        presenter.prepareBreakView();
    }

    @Override
    public void showInstructions() {
        presenter.prepareInstructionsView();

    }
}

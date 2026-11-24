package use_case.use_case_EvaluateGuess;

import entities.Game.CandleGame;

public class EvaluateGuessInputData {
    private String guess;
    private CandleGame game;

    public EvaluateGuessInputData(String guess, CandleGame game) {
        this.guess = guess;
        this.game = game;
    }

    /**
     * Returns the guessed code.
     * @return the guessed code .
     */
    public String getGuess() {
        return guess;
    }

    /**
     * Returns the game playing.
     * @return the game playing .
     */
    public CandleGame getGame() {
        return game;
    }

}

package use_case.CandleGame;
import entities.Game.GameCondition;



public class CandleGameOutputData {
    private final int maxTries; // holds the max tries user can make
    private final int attemptedTries ; //how many attempts made so that we can show it to the player
    private final GameCondition gameCondition; // If the game was Won, Lost, still going

    public CandleGameOutputData(int maxTries, GameCondition gameCondition, int attemptedTries) {
        this.maxTries = maxTries;
        this.attemptedTries = attemptedTries;
        this.gameCondition = GameCondition.ON_GOING;
    }

    public int getMaxTries() {
        return maxTries;
    }

    public int getAttemptedTries() {
        return attemptedTries;
    }

    public GameCondition getGameCondition() {
        return gameCondition;
    }
}

package use_case.CandleGame;
import entities.Game.GameCondition;



public class CandleGameOutputData {
    private final int maxTries; // holds the max tries user can make
    private final GameCondition gameCondition; // If the game was Won, Lost, still going

    public CandleGameOutputData(int maxTries, GameCondition gameCondition) {
        this.maxTries = maxTries;
        this.gameCondition = GameCondition.ON_GOING;
    }

    public int getMaxTries() {
        return maxTries;
    }

    public GameCondition getGameCondition() {
        return gameCondition;
    }
}

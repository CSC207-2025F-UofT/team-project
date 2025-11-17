package use_case.initialise_predictions;

import entity.Player;
import java.util.List;

/**
 * Output Data for Initialize Predictions use case.
 * Contains all players with their calculated predictions.
 */
public class InitialisePredictionsOutputData {
    private final List<Player> players;
    private final int totalPlayersProcessed;
    private final int gameweeksProcessed;

    public InitialisePredictionsOutputData(List<Player> players,
                                           int totalPlayersProcessed,
                                           int gameweeksProcessed) {
        this.players = players;
        this.totalPlayersProcessed = totalPlayersProcessed;
        this.gameweeksProcessed = gameweeksProcessed;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getTotalPlayersProcessed() {
        return totalPlayersProcessed;
    }

    public int getGameweeksProcessed() {
        return gameweeksProcessed;
    }
}
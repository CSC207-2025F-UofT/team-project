package use_case.viewplayerstats;

import java.util.List;

/**
 * Contains the player's statistics returned by the use case.
 * The data cannot be changed after construction.
 */
public final class ViewPlayerStatsOutputData {

    private final String playerId;
    private final int gamesPlayed;
    private final int gameWin;
    private final float winRate;
    private final int unoCallsMade;
    private final List<String> achievements;

    /**
     * Creates a snapshot of the player's statistics.
     *
     * @param playerId     the player's identifier
     * @param gamesPlayed  the total number of games played
     * @param gameWin      the number of games won
     * @param winRate      the win ratio from 0 to 1
     * @param unoCallsMade the number of UNO calls made
     * @param achievements the player's achievement names
     */
    public ViewPlayerStatsOutputData(String playerId,
                                     int gamesPlayed,
                                     int gameWin,
                                     float winRate,
                                     int unoCallsMade,
                                     List<String> achievements) {
        this.playerId = playerId;
        this.gamesPlayed = gamesPlayed;
        this.gameWin = gameWin;
        this.winRate = winRate;
        this.unoCallsMade = unoCallsMade;
        this.achievements = List.copyOf(achievements);
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGameWin() {
        return gameWin;
    }

    public float getWinRate() {
        return winRate;
    }

    public int getUnoCallsMade() {
        return unoCallsMade;
    }

    public List<String> getAchievements() {
        return achievements;
    }
}

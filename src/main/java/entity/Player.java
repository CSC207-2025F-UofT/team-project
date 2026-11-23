package entity;

/**
 * A player and their saved stats.
 * Ingrid adds recordWin and recordLoss on her branch.
 */
public class Player {
    private final String name;
    private int points;
    private int gamesPlayed;
    private int wins;
    private int currentStreak;
    private int longestStreak;
    /** guessDistribution[i] = number of games won on attempt i + 1. */
    private final int[] guessDistribution;

    /** A brand new player with every stat at 0. */
    public Player(String name) {
        this(name, 0, 0, 0, 0, 0, new int[GameRules.MAX_ATTEMPTS]);
    }

    /** A player loaded from storage. */
    public Player(String name, int points, int gamesPlayed, int wins,
                  int currentStreak, int longestStreak, int[] guessDistribution) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (guessDistribution.length != GameRules.MAX_ATTEMPTS) {
            throw new IllegalArgumentException(
                    "guessDistribution must have " + GameRules.MAX_ATTEMPTS + " entries");
        }
        this.name = name;
        this.points = points;
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.guessDistribution = guessDistribution.clone();
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    /** A copy of the guess distribution, so callers can't change the player's stats by accident. */
    public int[] getGuessDistribution() {
        return guessDistribution.clone();
    }
}

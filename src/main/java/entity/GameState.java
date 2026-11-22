package entity;

/**
 * Everything about one game in progress. apply(Outcome) is the single place
 * where an action changes the game, so every rule (clamping, losing, and
 * later time blocks, weather and end of day) is written exactly once.
 */
public class GameState {

    public static final int TOTAL_DAYS = 7;
    public static final String START_LOCATION_ID = "residence";

    private int day;
    private final Player player;
    private String currentLocationId;
    private GameStatus status;

    public GameState(int day, Player player, String currentLocationId) {
        this.day = day;
        this.player = player;
        this.currentLocationId = currentLocationId;
        this.status = GameStatus.IN_PROGRESS;
    }

    /** A new game: Day 1, starting stats, at the Residence. */
    public static GameState startNew() {
        return new GameState(1, Player.createNew(), START_LOCATION_ID);
    }

    /**
     * Applies an action's result to the game and returns the new status.
     * Later stories extend the body (time blocks, items, weather, end of day);
     * the signature stays the same so callers never change.
     */
    public GameStatus apply(Outcome outcome) {
        if (status == GameStatus.WON || status == GameStatus.LOST) {
            return status;
        }
        player.applyEffects(outcome.getEffects());
        if (outcome.getNewLocationId() != null) {
            currentLocationId = outcome.getNewLocationId();
        }
        if (player.isOutOfStat()) {
            status = GameStatus.LOST;
        }
        return status;
    }

    public int getDay() {
        return day;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public GameStatus getStatus() {
        return status;
    }
}
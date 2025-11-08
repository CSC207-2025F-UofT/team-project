package entity;
import java.util.List;

/**
 * An entity representing a fantasy premier league team.
 */
public class Team {
    private final List<Player> players;
    private final float budget;
    private final boolean isConfirmed;

    /**
     * Creates a new player with the given name, position, club, price, points, and selection status.
     * @param players the list of players in the team
     * @param budget the remaining budget for the team
     * @param isConfirmed whether the team has been finalized
     * @throws IllegalArgumentException if players is null or if budget is negative
     */
    public Team(List<Player> players, float budget, boolean isConfirmed) {
        if (players == null) {
            throw new IllegalArgumentException("Players list can't be null");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("Budget can't be negative");
        }

        this.players = players;
        this.budget = budget;
        this.isConfirmed = isConfirmed;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public float getBudget() {
        return budget;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
}

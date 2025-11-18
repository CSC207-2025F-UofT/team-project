package entity;

import java.util.List;

public class BestTeamResult {
    private final List<Player> players;
    private final double totalCost;
    private final double totalPredictedPoints;

    public BestTeamResult(List<Player> players, double totalCost, double totalPredictedPoints) {
        this.players = players;
        this.totalCost = totalCost;
        this.totalPredictedPoints = totalPredictedPoints;
    }

    public List<Player> getPlayers() {return players;}
    public double getTotalCost() {return totalCost;}
    public double getTotalPredictedPoints() {return totalPredictedPoints;}
}

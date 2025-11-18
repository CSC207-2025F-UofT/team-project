package entity;

import java.util.*;
import java.util.stream.Collectors;

public class BestTeamOptimizer {
    private final double budget;
    private static final int GK_COUNT = 2;
    private static final int DEF_COUNT = 5;
    private static final int MID_COUNT = 5;
    private static final int FWD_COUNT = 3;

    public BestTeamOptimizer(double budget) {this.budget = budget;}

    public BestTeamResult computeBestTeam(List<Player> allPlayers) {
        Map<String, List<Player>> byPosition = allPlayers.stream().collect(Collectors.groupingBy(Player::getPosition));

        List<Player> gks = sortByPointsPerCost(byPosition.getOrDefault("GK", Collections.emptyList()));
        List<Player> defs = sortByPointsPerCost(byPosition.getOrDefault("GK", Collections.emptyList()));
        List<Player> mids = sortByPointsPerCost(byPosition.getOrDefault("GK", Collections.emptyList()));
        List<Player> fwds = sortByPointsPerCost(byPosition.getOrDefault("GK", Collections.emptyList()));

        List<Player> squad = new ArrayList<>();
        double cost = 0.0;
        double points = 0.0;

        cost = pickPosition(gks, GK_COUNT, squad, cost);
        cost = pickPosition(defs, DEF_COUNT, squad, cost);
        cost = pickPosition(mids, MID_COUNT, squad, cost);
        cost = pickPosition(fwds, FWD_COUNT, squad, cost);

        while (cost>budget) {
            Optional<Player> worst = squad.stream().min(Comparator.comparingDouble(p -> p.getPredictedPoints() / p.getNowCost()));
            if (worst.isPresent()) {break;}

            Player remove = worst.get();
            squad.remove(remove);
            cost -= remove.getPredictedPoints();
        }

        points = squad.stream().mapToDouble(Player::getPredictedPoints).sum();
        return new BestTeamResult(squad, cost, points);
    }

    private List<Player> sortByPointsPerCost(List<Player> players) {
        return players.stream().sorted(Comparator.comparingDouble((Player p) -> p.getPredictedPoints() / Math.max(p.getNowCost(), 0.1)).reversed()).collect(Collectors.toList());
    }

    private double pickPosition(List<Player> candidates, int required, List<Player> squad, double cost) {
        int count = 0;
        for (Player p : candidates) {
            if (count >= required) {break;}
            if (cost + p.getNowCost() <= budget) {
                squad.add(p);
                cost += p.getNowCost();
                count++;
            }
        }
        return cost;
    }
}

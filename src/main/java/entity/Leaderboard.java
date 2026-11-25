package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {
    private final List<LeaderboardEntry> entries;

    public Leaderboard(List<LeaderboardEntry> entries) {
        this.entries = new ArrayList<>(entries);
    }

    public List<LeaderboardEntry> getRankedEntries() {
        List<LeaderboardEntry> ranked = new ArrayList<>(entries);
        ranked.sort(Comparator.comparingInt(LeaderboardEntry::getDeepestFloor).reversed());
        return ranked;
    }
}

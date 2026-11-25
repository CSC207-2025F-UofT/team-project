package use_case.leaderboard;

import entity.LeaderboardEntry;

import java.util.List;

public interface LeaderboardDataAccessInterface {
    List<LeaderboardEntry> getAllEntries();
}

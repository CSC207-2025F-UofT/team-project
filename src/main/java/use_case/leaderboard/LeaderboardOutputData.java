package use_case.leaderboard;

import java.util.List;

public class LeaderboardOutputData {
    private final List<String> usernames;
    private final List<Integer> floors;

    public LeaderboardOutputData(List<String> usernames, List<Integer> floors) {
        this.usernames = usernames;
        this.floors = floors;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public List<Integer> getFloors() {
        return floors;
    }
}

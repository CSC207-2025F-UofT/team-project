package entity;

public class LeaderboardEntry {
    private final String username;
    private final int deepestFloor;

    public LeaderboardEntry(String username, int deepestFloor) {
        this.username = username;
        this.deepestFloor = deepestFloor;
    }

    public String getUsername() {
        return username;
    }

    public int getDeepestFloor() {
        return deepestFloor;
    }
}

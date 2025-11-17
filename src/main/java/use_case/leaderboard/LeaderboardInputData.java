package use_case.leaderboard;

import entity.User;

/**
 * The Input Data for the Leaderboard.
 */

public class LeaderboardInputData {
    private User user = null;

    public LeaderboardInputData(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

}

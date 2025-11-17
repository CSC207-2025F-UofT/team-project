package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;

public interface LeaderboardUserDataAccessInterface {
    ArrayList<User> getTopUsers(int topN);
    int getUserRank(User user);
}

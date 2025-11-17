package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardOutputData {
    private ArrayList<User> top5Users;
    private User currentUser;
    private int currentUserRank;
    public LeaderboardOutputData(ArrayList<User> top5Users, User currentUser, int currentUserRank) {
        this.top5Users = top5Users;
        this.currentUser = currentUser;
        this.currentUserRank = currentUserRank;
    }
    public ArrayList<User> getTop5Users() {
        return top5Users;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public int getCurrentUserRank() {
        return currentUserRank;
    }
}

package use_case.leaderboard;

import entity.Leaderboard;
import entity.LeaderboardEntry;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardInteractor implements LeaderboardInputBoundary {
    private final LeaderboardDataAccessInterface dataAccess;
    private final LeaderboardOutputBoundary presenter;

    public LeaderboardInteractor(LeaderboardDataAccessInterface dataAccess,
                                 LeaderboardOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        List<LeaderboardEntry> leaderboardEntries = dataAccess.getAllEntries();
        Leaderboard leaderboard = new Leaderboard(leaderboardEntries);
        List<LeaderboardEntry> rankedEntries = leaderboard.getRankedEntries();

        List<String> usernames = new ArrayList<>();
        List<Integer> floors = new ArrayList<>();

        for (LeaderboardEntry leaderboardEntry : rankedEntries) {
            usernames.add(leaderboardEntry.getUsername());
            floors.add(leaderboardEntry.getDeepestFloor());
        }
        LeaderboardOutputData leaderboardOutputData = new LeaderboardOutputData(usernames, floors);
        presenter.prepareSuccessView(leaderboardOutputData);
    }
}

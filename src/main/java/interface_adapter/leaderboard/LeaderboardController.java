package interface_adapter.leaderboard;

import use_case.leaderboard.LeaderboardInputBoundary;

public class LeaderboardController {
    private final LeaderboardInputBoundary interactor;

    public LeaderboardController(LeaderboardInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}

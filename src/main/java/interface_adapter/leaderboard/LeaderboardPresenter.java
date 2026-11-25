package interface_adapter.leaderboard;

import interface_adapter.save.SaveState;
import interface_adapter.save.SaveViewModel;
import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.leaderboard.LeaderboardOutputData;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardPresenter implements LeaderboardOutputBoundary {
    private final SaveViewModel viewModel;

    public LeaderboardPresenter(SaveViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LeaderboardOutputData outputData) {
        List<String> usernames = outputData.getUsernames();
        List<Integer> floors = outputData.getFloors();

        List<String> lines = new ArrayList<>();
        if (usernames.isEmpty()) {
            lines.add("No scores yet.");
        }
        for (int i = 0; i < usernames.size(); i++) {
            // i starts at 0, so i + 1 gives ranks starting at 1
            lines.add((i+1) + ". " +  usernames.get(i) + " - floor " + floors.get(i));
        }

        SaveState state = viewModel.getState();
        state.setLeaderboardLines(lines);
        viewModel.notifyStateChanged();
    }
}

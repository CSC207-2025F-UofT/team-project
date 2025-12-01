package use_case.show_results;

import entity.AdventureGame;
import entity.Location;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interactor for showing game results.
 */
public class ShowResultsInteractor implements ShowResultsInputBoundary {
    private final ShowResultsGameDataAccessInterface gameDataAccess;
    private final ShowResultsOutputBoundary presenter;

    public ShowResultsInteractor(ShowResultsGameDataAccessInterface gameDataAccess,
                                 ShowResultsOutputBoundary presenter) {
        this.gameDataAccess = gameDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ShowResultsInputData showResultsInputData) {
        final AdventureGame game = gameDataAccess.get();

        final String userName = "Adventurer";
        final List<String> pathHistory = game.getPathHistory().stream()
                .map(Location::getName)
                .collect(Collectors.toList());
        final int totalMoves = pathHistory.size();
        final String finalLocation = game.getGameMap().getCurrentLocation().getName();

        final ShowResultsOutputData outputData = new ShowResultsOutputData(
                userName, totalMoves, pathHistory, finalLocation
        );

        // Clear game data to force a new game
        gameDataAccess.clearGameData();

        presenter.prepareSuccessView(outputData);
    }
}

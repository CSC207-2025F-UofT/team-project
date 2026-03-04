package use_case.ResultScreen;

import entity.AdventureGame;
import entity.Location;
import use_case.show_results.ShowResultsGameDataAccessInterface;
import use_case.show_results.ShowResultsInputData;
import use_case.show_results.ShowResultsOutputBoundary;
import use_case.show_results.ShowResultsOutputData;

import java.util.List;
import java.util.stream.Collectors;

public class ResultScreenInteractor implements ResultScreenInputBoundary {

//    private final ResultScreenOutputBoundary presenter;
//
//    public ResultScreenInteractor(ResultScreenOutputBoundary presenter) {
//        this.presenter = presenter;
//    }
//
//    @Override
//    public void execute(ResultScreenInputData inputData) {
//
//        if (!inputData.isGameCompleted()) {
//            presenter.prepareSuccessView(
//                    new ResultScreenOutputData("Error: Game is not completed")
//            );
//            return;
//        }
//
//        ResultScreenOutputData output =
//                new ResultScreenOutputData("Game Completed!");
//
//        presenter.prepareSuccessView(output);
//    }

    private ResultScreenGameDataAccesssInterface gameDataAccess;
    private ShowResultsOutputBoundary presenter;

    public void ShowResultsInteractor(ShowResultsGameDataAccessInterface gameDataAccess,
                                      ShowResultsOutputBoundary presenter) {
        this.gameDataAccess = (ResultScreenGameDataAccesssInterface) gameDataAccess;
        this.presenter = presenter;
    }

    public ResultScreenInteractor(ResultScreenGameDataAccesssInterface gameDataAccess, ShowResultsOutputBoundary presenter) {
        this.gameDataAccess = gameDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ResultScreenInputData inputData) {
        //NA
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

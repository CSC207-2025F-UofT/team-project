package interface_adapter.create_teams;

import use_case.start_game.StartGameInputBoundary;
import use_case.start_game.StartGameInputData;

public class StartGameController {
    private final StartGameInputBoundary startGameUseCaseInteractor;

    public StartGameController(StartGameInputBoundary startGameUseCaseInteractor) {
        this.startGameUseCaseInteractor = startGameUseCaseInteractor;
    }

    /**
     * Executes the Load Question use case.
     */
    public void execute(String team1Name, String team2Name) {
        final StartGameInputData startGameInputData = new StartGameInputData(team1Name, team2Name);

        startGameUseCaseInteractor.execute(startGameInputData);
    }
}

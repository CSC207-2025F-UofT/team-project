package interface_adapter.create_teams;

import interface_adapter.ViewManagerModel;
import interface_adapter.game_running.GameRunningState;
import use_case.start_game.StartGameOutputData;
import use_case.start_game.StartGameOutputBoundary;
import interface_adapter.game_running.GameRunningViewModel;


public class StartGamePresenter implements StartGameOutputBoundary {
    private final CreateTeamViewModel createTeamViewModel;
    private final GameRunningViewModel gameRunningViewModel;
    private final ViewManagerModel viewManagerModel;

    public StartGamePresenter(ViewManagerModel viewManagerModel, CreateTeamViewModel createTeamViewModel, GameRunningViewModel gameRunningViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.createTeamViewModel = createTeamViewModel;
        this.gameRunningViewModel = gameRunningViewModel;
    }

    @Override
    public void prepareSuccessView(StartGameOutputData teams) {
        final GameRunningState gameRunningState = gameRunningViewModel.getState();

        //Set the game running team names
        gameRunningState.setTeam1Name(teams.getTeam1Name());
        gameRunningState.setTeam2Name(teams.getTeam2Name());
        gameRunningViewModel.notifyStateChanged();
        
        // Change the main board
        viewManagerModel.setState(GameRunningViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final CreateTeamState state = createTeamViewModel.getState();
        state.setLoadQuestionError(error);
        createTeamViewModel.notifyStateChanged();
    }
}

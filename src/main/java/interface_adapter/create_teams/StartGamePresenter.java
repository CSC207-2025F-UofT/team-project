package interface_adapter.create_teams;

import interface_adapter.ViewManagerModel;
import use_case.start_game.StartGameOutputData;
import use_case.start_game.StartGameOutputBoundary;


public class StartGamePresenter implements StartGameOutputBoundary {
    private final CreateTeamViewModel createTeamViewModel;
    private final ViewManagerModel viewManagerModel;

    public StartGamePresenter(ViewManagerModel viewManagerModel, CreateTeamViewModel createTeamViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.createTeamViewModel = createTeamViewModel;
    }

    @Override
    public void prepareSuccessView(StartGameOutputData teams) {
        // On success, update the createTeamViewModel's state
        final CreateTeamState state = createTeamViewModel.getState();
        // Update any state stuff here
        createTeamViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final CreateTeamState state = createTeamViewModel.getState();
        state.setLoadQuestionError(error);
        createTeamViewModel.notifyStateChanged();
    }
}

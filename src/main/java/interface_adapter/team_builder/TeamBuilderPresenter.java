package interface_adapter.team_builder;

import interface_adapter.ViewManagerModel;
import use_case.BuildPokemonTeam.BuildPokemonTeamOutputBoundary;
import use_case.BuildPokemonTeam.BuildPokemonTeamOutputData;

public class TeamBuilderPresenter implements BuildPokemonTeamOutputBoundary {

    private final TeamBuilderViewModel teamBuilderViewModel;
    private final ViewManagerModel viewManagerModel;

    public TeamBuilderPresenter(TeamBuilderViewModel teamBuilderViewModel, ViewManagerModel viewManagerModel) {
        this.teamBuilderViewModel = teamBuilderViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(BuildPokemonTeamOutputData outputData) {
        final TeamBuilderState teamBuilderState = teamBuilderViewModel.getState();
        teamBuilderState.setTeam(outputData.getTeam());
        teamBuilderViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final TeamBuilderState teamBuilderState = teamBuilderViewModel.getState();
        teamBuilderState.setTeamNameError(errorMessage);
        teamBuilderViewModel.firePropertyChange();
    }
}

package interface_adapter.team_builder;

import entity.Team;
import use_case.BuildPokemonTeam.BuildPokemonTeamInputBoundary;
import use_case.BuildPokemonTeam.BuildPokemonTeamInputData;

import java.io.IOException;

public class TeamBuilderController {

    private final BuildPokemonTeamInputBoundary userTeamBuilderUseCaseInteractor;

    public TeamBuilderController(BuildPokemonTeamInputBoundary userTeamBuilderUseCaseInteractor) {
        this.userTeamBuilderUseCaseInteractor = userTeamBuilderUseCaseInteractor;
    }

    public void addToTeam(String name, Team team, int index) throws IOException {
        final BuildPokemonTeamInputData buildPokemonTeamInputData = new BuildPokemonTeamInputData(name, team, index);

        userTeamBuilderUseCaseInteractor.addToTeam(buildPokemonTeamInputData);
    }

    public void addToTeam(String name, Team team) throws IOException {
        final BuildPokemonTeamInputData buildPokemonTeamInputData = new BuildPokemonTeamInputData(name, team);

        userTeamBuilderUseCaseInteractor.addToTeam(buildPokemonTeamInputData);
    }

    public void removeFromTeam(String name, Team team, int index) throws IOException {
        final BuildPokemonTeamInputData buildPokemonTeamInputData = new BuildPokemonTeamInputData(name, team, index);
        userTeamBuilderUseCaseInteractor.removeFromTeam(buildPokemonTeamInputData);
    }

    public void saveTeam(String name, Team team) throws IOException {
        final BuildPokemonTeamInputData buildPokemonTeamInputData = new BuildPokemonTeamInputData(name, team);
        userTeamBuilderUseCaseInteractor.saveTeam(buildPokemonTeamInputData);
    }

    public void switchToPokemonLookupView(int index) {
        userTeamBuilderUseCaseInteractor.switchToPokemonLookupView(index);
    }

}

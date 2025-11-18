package use_case.BuildPokemonTeam;
import data_access.BuildPokemonTeamDataAccessObject;
import entity.Team;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import entity.Pokemon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BuildPokemonTeamInteractor implements BuildPokemonTeamInputBoundary {
    private final BuildPokemonTeamOutputBoundary userPresenter;
    private final Pokemon Pokemon;


    public BuildPokemonTeamInteractor(BuildPokemonTeamOutputBoundary buildPokemonTeamOutputBoundary,
                                      Pokemon Pokemon) {
        this.userPresenter = buildPokemonTeamOutputBoundary;
        this.Pokemon = Pokemon;
    }


    @Override
    public void addToTeam(BuildPokemonTeamInputData buildPokemonTeamInputData) throws IOException {

            final int index = buildPokemonTeamInputData.getIndex();
            final Team team = buildPokemonTeamInputData.getTeam();


            // Checks to see if the team can even be added to.
            if (index == -1){
                // Goes through each team slot to see if there's a slot that can be added to.
                for ( int i = 0; i < 6; i++) {
                    if (team.getPokemon(i) == null){
                        team.setPokemon(Pokemon, i);
                        final BuildPokemonTeamOutputData buildPokemonTeamOutputData =
                                new BuildPokemonTeamOutputData(team);
                        userPresenter.prepareSuccessView(buildPokemonTeamOutputData);
                        return;
                    }
                }
                userPresenter.prepareFailView("Team is full. Please remove a Pokemon or create a new Team.");
                return;
            }

            team.setPokemon(Pokemon, index);

            final BuildPokemonTeamOutputData buildPokemonTeamOutputData =
                    new BuildPokemonTeamOutputData(team);
            userPresenter.prepareSuccessView(buildPokemonTeamOutputData);

        }

    @Override
    public void removeFromTeam(BuildPokemonTeamInputData buildPokemonTeamInputData) throws IOException {

        final int index = buildPokemonTeamInputData.getIndex();
        final Team team = buildPokemonTeamInputData.getTeam();
        team.setPokemon(null, index);

        final BuildPokemonTeamOutputData buildPokemonTeamOutputData =
                new BuildPokemonTeamOutputData(team);
        userPresenter.prepareSuccessView(buildPokemonTeamOutputData);

    }

    @Override
    public void saveTeam(BuildPokemonTeamInputData buildPokemonTeamInputData) throws IOException {
        final BuildPokemonTeamDataAccessObject dataAccessObject = new BuildPokemonTeamDataAccessObject();
        final Team team = buildPokemonTeamInputData.getTeam();
        dataAccessObject.saveTeam(team);
    }
}

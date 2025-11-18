package use_case.BuildPokemonTeam;

import entity.Pokemon;
import entity.EmptyPokemonFactory;
import entity.Team;
import org.junit.Test;
import use_case.BuildPokemonTeam.BuildPokemonTeamInputData;
import use_case.BuildPokemonTeam.BuildPokemonTeamInteractor;
import use_case.BuildPokemonTeam.BuildPokemonTeamOutputBoundary;
import use_case.BuildPokemonTeam.BuildPokemonTeamOutputData;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestBuildPokemonTeam {

    @Test
    public void BuildPokemonTeamTest() throws IOException {
        Team t = new Team("Chuckle Sandwich");
        EmptyPokemonFactory x = new EmptyPokemonFactory();
        Pokemon a = x.create();
        Pokemon b = x.create();
        Pokemon c = x.create();
        Pokemon d = x.create();
        Pokemon e = x.create();
        Pokemon f = x.create();

        BuildPokemonTeamInputData inputData = new BuildPokemonTeamInputData(a.getName(), t, 0);
        BuildPokemonTeamOutputBoundary successPresenter = new BuildPokemonTeamOutputBoundary() {
            @Override
            public void prepareSuccessView(BuildPokemonTeamOutputData outputData) {
                assertEquals(t, outputData.getTeam());
                assertEquals(a, t.getPokemon(0));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(errorMessage);

            }
        };

        BuildPokemonTeamInteractor interactor = new BuildPokemonTeamInteractor(successPresenter, a);
        interactor.execute(inputData);




    }
}

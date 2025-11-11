package use_case.lookup;

import entity.GradingStrategy;
import entity.Pokemon;

import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.Test;
import use_case.PokemonLookup.*;

import static org.junit.jupiter.api.Assertions.*;

class TestPokemonLookupInteractor {

    @Test
    void MagikarpTest() throws IOException {
        ArrayList<Integer> statsikarp = new ArrayList<>(Arrays.asList(20, 10, 55, 15, 20, 80));
        ArrayList<Integer> abilitykarp = new ArrayList<>(Arrays.asList(33));
        ArrayList<Integer> moveskarp = new ArrayList<>(Arrays.asList(33, 56, 150, 175, 340));
        ArrayList<Integer> eggkarp = new ArrayList<>(Arrays.asList(12, 14));
        ArrayList<Integer> pokedexkarp = new ArrayList<>(Arrays.asList(129, 129, 76, 52, 23, 23, 76, 13, 49, 53, 91, 91, 34, 51, 41, 111, 34, 54, 53, 129, 144, 42, 62, 80, 134, 43, 32));


        Pokemon magikarp = new Pokemon("pikachu", 11, 0, statsikarp, abilitykarp, 155, moveskarp, eggkarp, pokedexkarp);
        PokemonLookupInputData inputData = new PokemonLookupInputData("magikarp");
        PokemonLookupOutputBoundary successPresenter = new PokemonLookupOutputBoundary() {
            @Override
            public void prepareSuccessView(PokemonLookupOutputData outputData) {
                assertEquals(magikarp, outputData.getPokemon());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use Case failure is unexpected.");
            }
        };
        PokemonLookupInputBoundary interactor = new PokemonLookupInteractor(successPresenter, magikarp);
        interactor.execute(inputData);
    }}
package use_case.lookup;

import entity.Pokemon;
import entity.EmptyPokemonFactory;

import java.io.IOException;
import java.util.*;
import java.util.HashSet;

import entity.Type;
import org.junit.jupiter.api.Test;
import use_case.PokemonLookup.*;

import static org.junit.jupiter.api.Assertions.*;

class TestPokemonLookupInteractor {


    // a basic pokemon test
    @Test
    void MagikarpTest() throws IOException {
        ArrayList<Integer> statsikarp = new ArrayList<>(Arrays.asList(20, 10, 55, 15, 20, 80));
        ArrayList<Integer> abilitykarp = new ArrayList<>(Arrays.asList(33));
        ArrayList<Integer> moveskarp = new ArrayList<>(Arrays.asList(33, 56, 150, 175, 340));
        ArrayList<Integer> eggkarp = new ArrayList<>(Arrays.asList(12, 14));
        ArrayList<Integer> pokedexkarp = new ArrayList<>(Arrays.asList(129, 129, 76, 52, 23, 23, 76, 13, 49, 53, 91, 91, 34, 51, 41, 111, 34, 54, 53, 129, 144, 42, 62, 80, 134, 43, 32));
        HashSet<String> waterstrength = new HashSet<>(Arrays.asList("fire", "rock", "ground"));
        HashSet<String> waterweak = new HashSet<>(Arrays.asList("grass", "electric"));
        HashSet<String> wateres = new HashSet<>(Arrays.asList("fire", "rock", "ground"));

        Type type1 = new Type("water", 11,
                waterstrength, waterweak, wateres);
        Pokemon magikarp = new Pokemon("magikarp", type1, null, statsikarp, abilitykarp, 155, moveskarp, eggkarp, pokedexkarp, "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/129.png");
        PokemonLookupInputData inputData = new PokemonLookupInputData("magikarp");
        PokemonLookupOutputBoundary successPresenter = new PokemonLookupOutputBoundary() {
            @Override
            public void prepareSuccessView(PokemonLookupOutputData outputData) {
                assertEquals(magikarp, outputData.getPokemon());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(errorMessage);
            }
        };
        PokemonLookupInputBoundary interactor = new PokemonLookupInteractor(successPresenter, magikarp);
        interactor.execute(inputData);
    }

    // dual type return the right weakness test
    @Test
    void LudicoloTest() throws IOException {
//        HashSet<String> waterstrength = new HashSet<>(Arrays.asList("fire", "rock", "ground"));
//        HashSet<String> waterweak = new HashSet<>(Arrays.asList("grass", "electric"));
//        HashSet<String> wateres = new HashSet<>(Arrays.asList("fire", "rock", "ground"));
//
//        HashSet<String> grassstrength = new HashSet<>(Arrays.asList("ground", "rock", "water"));
//        HashSet<String> grassweak = new HashSet<>(Arrays.asList("flying", "fire", "bug", "ice", "poison"));
//        HashSet<String> grassres = new HashSet<>(Arrays.asList("ground", "grass", "water", "electric"));
//        Type type1 = new Type("water", 11,
//                waterstrength, waterweak, wateres);
//        Type type2 = new Type("grass", 12,
//                grassstrength, grassweak, grassres);
        EmptyPokemonFactory factoree = new EmptyPokemonFactory();
        Pokemon emptymon = factoree.create();
//        emptymon.setType1(type1);
//        emptymon.setType2(type2);
        HashSet<String> ludicoloweakness = new HashSet<>(Arrays.asList("poison", "flying", "bug"));
        PokemonLookupInputData inputData = new PokemonLookupInputData("ludicolo");
        PokemonLookupOutputBoundary successPresenter = new PokemonLookupOutputBoundary() {
            @Override
            public void prepareSuccessView(PokemonLookupOutputData outputData) {
                assertEquals(ludicoloweakness, outputData.getPokemon().getWeaknesses());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(errorMessage);
            }
        };
        PokemonLookupInputBoundary interactor = new PokemonLookupInteractor(successPresenter, emptymon);
        interactor.execute(inputData);
    }

    // dual type return the right strengths test (no duplicates)
    @Test
    void AurorusTest() throws IOException {
//        HashSet<String> icestrength = new HashSet<>(Arrays.asList("flying", "ground", "grass", "dragon"));
//        HashSet<String> iceweak = new HashSet<>(Arrays.asList("fighting", "fire", "rock", "steel"));
//        HashSet<String> iceres = new HashSet<>(Arrays.asList("ice"));
//
//        HashSet<String> rockstrength = new HashSet<>(Arrays.asList("flying", "bug", "fire", "ice"));
//        HashSet<String> rockweak = new HashSet<>(Arrays.asList("water", "grass", "ground", "steel", "fighting"));
//        HashSet<String> rockres = new HashSet<>(Arrays.asList("ground", "grass", "water", "electric"));
//        Type type2 = new Type("ice", 15,
//                icestrength, iceweak, iceres);
//        Type type1 = new Type("rock", 6,
//                rockstrength, rockweak, rockres);
        EmptyPokemonFactory factoree = new EmptyPokemonFactory();
        Pokemon emptymon = factoree.create();
        HashSet<String> aurorusstrength = new HashSet<>(Arrays.asList("flying", "ground", "grass", "dragon", "bug", "fire", "ice"));
//        emptymon.setType1(type1);
//        emptymon.setType2(type2);
        PokemonLookupInputData inputData = new PokemonLookupInputData("aurorus");
        PokemonLookupOutputBoundary successPresenter = new PokemonLookupOutputBoundary() {
            @Override
            public void prepareSuccessView(PokemonLookupOutputData outputData) {
                assertEquals(aurorusstrength, outputData.getPokemon().getStrengths());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(errorMessage);
            }
        };
        PokemonLookupInputBoundary interactor = new PokemonLookupInteractor(successPresenter, emptymon);
        interactor.execute(inputData);
    }

    // dual types return the right resistances

    @Test
    void DurantTest() throws IOException {
//        HashSet<String> bugstrength = new HashSet<>(Arrays.asList("grass", "dark", "psychic"));
//        HashSet<String> bugweak = new HashSet<>(Arrays.asList("fire", "rock", "flying"));
//        HashSet<String> bugres = new HashSet<>(Arrays.asList("ground", "grass", "fighting"));
//
//        HashSet<String> steelstrength = new HashSet<>(Arrays.asList("rock", "ice", "fairy"));
//        HashSet<String> steelweak = new HashSet<>(Arrays.asList("fire", "fighting", "ground"));
//        HashSet<String> steelres = new HashSet<>(Arrays.asList("normal", "flying", "rock", "bug", "steel", "grass", "psychic", "ice", "dragon", "fairy"));
//        Type type1 = new Type("bug", 7,
//                bugstrength, bugweak, bugres);
//        Type type2 = new Type("steel", 9,
//                steelstrength, steelweak, steelres);
        EmptyPokemonFactory factoree = new EmptyPokemonFactory();
        Pokemon emptymon = factoree.create();
//        emptymon.setType1(type1);
//        emptymon.setType2(type2);
        HashSet<String> durantres = new HashSet<>(Arrays.asList("normal", "bug", "poison", "steel", "grass", "psychic", "ice", "dragon", "fairy"));
        PokemonLookupInputData inputData = new PokemonLookupInputData("durant");
        PokemonLookupOutputBoundary successPresenter = new PokemonLookupOutputBoundary() {
            @Override
            public void prepareSuccessView(PokemonLookupOutputData outputData) {
                assertEquals(durantres, outputData.getPokemon().getResistances());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(errorMessage);
            }
        };
        PokemonLookupInputBoundary interactor = new PokemonLookupInteractor(successPresenter, emptymon);
        interactor.execute(inputData);
    }

    // pokemon only has one ability
    @Test
    void ShedinjaTest() throws IOException {
        ArrayList<Integer> abilitykarp = new ArrayList<>(Arrays.asList(25));
        EmptyPokemonFactory factoree = new EmptyPokemonFactory();
        Pokemon emptymon = factoree.create();
        emptymon.setAbilities(abilitykarp);
        PokemonLookupInputData inputData = new PokemonLookupInputData("shedinja");
        PokemonLookupOutputBoundary successPresenter = new PokemonLookupOutputBoundary() {
            @Override
            public void prepareSuccessView(PokemonLookupOutputData outputData) {
                assertEquals(emptymon.getAbilities(), outputData.getPokemon().getAbilities());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(errorMessage);
            }
        };
        PokemonLookupInputBoundary interactor = new PokemonLookupInteractor(successPresenter, emptymon);
        interactor.execute(inputData);
    }

    // pokemon only has one ability and one hidden ability
    @Test
    void GarchompTest() throws IOException {
        ArrayList<Integer> ability = new ArrayList<>(Arrays.asList(8));
        EmptyPokemonFactory factoree = new EmptyPokemonFactory();
        Pokemon emptymon = factoree.create();
        emptymon.setAbilities(ability);
        emptymon.setHidden(24);
        PokemonLookupInputData inputData = new PokemonLookupInputData("garchomp");
        PokemonLookupOutputBoundary successPresenter = new PokemonLookupOutputBoundary() {
            @Override
            public void prepareSuccessView(PokemonLookupOutputData outputData) {
                assertEquals(emptymon.getAbilities(), outputData.getPokemon().getAbilities());
                assertEquals(24, outputData.getPokemon().getHidden());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(errorMessage);
            }
        };
        PokemonLookupInputBoundary interactor = new PokemonLookupInteractor(successPresenter, emptymon);
        interactor.execute(inputData);
    }

    // pokemon only has all three abilities
    @Test
    void ScolipedeTest() throws IOException {
        ArrayList<Integer> ability = new ArrayList<>(Arrays.asList(38, 68));
        EmptyPokemonFactory factoree = new EmptyPokemonFactory();
        Pokemon emptymon = factoree.create();
        emptymon.setAbilities(ability);
        emptymon.setHidden(3);
        PokemonLookupInputData inputData = new PokemonLookupInputData("scolipede");
        PokemonLookupOutputBoundary successPresenter = new PokemonLookupOutputBoundary() {
            @Override
            public void prepareSuccessView(PokemonLookupOutputData outputData) {
                assertEquals(emptymon.getAbilities(), outputData.getPokemon().getAbilities());
                assertEquals(3, outputData.getPokemon().getHidden());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(errorMessage);
            }
        };
        PokemonLookupInputBoundary interactor = new PokemonLookupInteractor(successPresenter, emptymon);
        interactor.execute(inputData);
    }

}




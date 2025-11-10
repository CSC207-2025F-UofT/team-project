package use_case.BuildPokemonTeam;

import entity.Pokemon;

import java.util.ArrayList;

public class TestBuildPokemonTeam {
    public static void main(String[] args) {
        try {
            BuildPokemonTeamOutputBoundary presenter = new BuildPokemonTeamOutputBoundary() {
                @Override
                public void prepareSuccessView(BuildPokemonTeamOutputData data) {
                    System.out.println("✅ Success!");
                    System.out.println("Pokemon: " + data.getPokemon().getName());
                }

                @Override
                public void prepareFailView(String error) {
                    System.out.println("❌ Error: " + error);
                }
            };

            String name = "pikachu" ;
            int type1 = 0;
            int type2 = 0;
            ArrayList<Integer> stats = new ArrayList<>();
            ArrayList<Integer> abilities = new ArrayList<>();
            int hidden = 0;
            ArrayList<Integer> moves = new ArrayList<>();
            ArrayList<Integer> egggroup = new ArrayList<>();
            ArrayList<Integer> pokedexes = new ArrayList<>();




            Pokemon pokemon = new Pokemon(name, type1, type2, stats, abilities, hidden,  moves, egggroup, pokedexes);

            // Create the interactor
            BuildPokemonTeamInteractor interactor = new BuildPokemonTeamInteractor(presenter, pokemon);

            // Provide some test input data
            BuildPokemonTeamInputData input = new BuildPokemonTeamInputData("pikachu");

            // Run it
            interactor.execute(input);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

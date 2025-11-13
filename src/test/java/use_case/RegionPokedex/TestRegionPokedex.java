package use_case.RegionPokedex;

import entity.Pokemon;
import use_case.seeRegionPokedex.RegionPokedexInputData;
import use_case.seeRegionPokedex.RegionPokedexInteractor;
import use_case.seeRegionPokedex.RegionPokedexOutputBoundary;
import use_case.seeRegionPokedex.RegionPokedexOutputData;
import java.util.ArrayList;


public class TestRegionPokedex {
    public static void main(String[] args) {
        try {
            RegionPokedexOutputBoundary presenter = new RegionPokedexOutputBoundary() {
                @Override
                public void prepareSuccessView(RegionPokedexOutputData data) {
                    System.out.println("prepareSuccessView");
                    ArrayList<String> names = data.getPokemonNames();

                    System.out.println("Total Pokemon: " + names.size());
                }

                @Override
                public void prepareFailureView(String error) {
                    System.out.println("prepareFailView" + error);
                }
            };
            Pokemon dummy = new Pokemon(
                    "dummy",
                    0,
                    0,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    0,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );

            RegionPokedexInteractor interactor = new RegionPokedexInteractor(dummy, presenter);
            RegionPokedexInputData input = new RegionPokedexInputData("kanto");
            interactor.execute(input);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

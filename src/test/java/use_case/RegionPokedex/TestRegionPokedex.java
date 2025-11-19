package use_case.RegionPokedex;

import entity.EmptyPokemonFactory;
import entity.Pokemon;
import org.junit.jupiter.api.Test;
import use_case.seeRegionPokedex.RegionPokedexInputData;
import use_case.seeRegionPokedex.RegionPokedexInteractor;
import use_case.seeRegionPokedex.RegionPokedexOutputBoundary;
import use_case.seeRegionPokedex.RegionPokedexOutputData;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class TestRegionPokedex {

    @Test
    void correctLengthTest() throws IOException {
        RegionPokedexOutputBoundary presenter = new RegionPokedexOutputBoundary() {
            @Override
            public void prepareSuccessView(RegionPokedexOutputData data) {
                assertEquals(151, data.getPokemonNames().size());
            }

            @Override
            public void prepareFailureView(String error) {
                System.out.println("prepareFailView" + error);
            }
        };
        Pokemon dummy = EmptyPokemonFactory.create();

        RegionPokedexInteractor interactor = new RegionPokedexInteractor(dummy, presenter);
        RegionPokedexInputData input = new RegionPokedexInputData("kanto");
        interactor.execute(input);

    }

}

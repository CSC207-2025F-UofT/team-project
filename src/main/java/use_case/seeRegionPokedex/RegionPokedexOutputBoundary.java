package use_case.seeRegionPokedex;

public interface RegionPokedexOutputBoundary {
    void prepareSuccessView(RegionPokedexOutputData pokemonOutputData);
    void prepareFailureView(String errorMessage);
}

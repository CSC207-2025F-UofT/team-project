package use_case.filter;


public interface FilterPokemonOutputBoundary {


    void prepareSuccessView(FilterPokemonOutputData responseModel);


    void prepareFailView(String errorMessage);
}
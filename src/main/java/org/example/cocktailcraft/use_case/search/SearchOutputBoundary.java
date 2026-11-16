package org.example.cocktailcraft.use_case.search;

public interface SearchOutputBoundary {
    void prepareSuccessView(SearchOutputData outputData);

    void prepareFailView(String message);
}

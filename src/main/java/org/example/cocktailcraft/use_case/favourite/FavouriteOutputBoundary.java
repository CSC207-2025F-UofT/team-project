package org.example.cocktailcraft.use_case.favourite;

public interface FavouriteOutputBoundary {
    void prepareSuccessView(FavouriteOutputData outputData);

    void prepareFailView(String message);
}

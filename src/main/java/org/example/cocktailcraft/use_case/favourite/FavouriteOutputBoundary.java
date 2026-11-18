package org.example.cocktailcraft.use_case.favourite;

public interface FavouriteOutputBoundary {
    /**
     * Prepares the success view for the Favourite Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(FavouriteOutputData outputData);

    /**
     * Prepares the failure view for the Favourite Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}

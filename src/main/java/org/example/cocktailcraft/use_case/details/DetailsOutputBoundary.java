package org.example.cocktailcraft.use_case.details;

public interface DetailsOutputBoundary {
    void prepareSuccessView(DetailsOutputData outputData);

    void prepareFailView(String message);
}

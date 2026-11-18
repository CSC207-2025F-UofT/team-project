package org.example.cocktailcraft.use_case.customize;

public interface CustomizeOutputBoundary {
    void prepareSuccessView(CustomizeOutputData outputData);

    void prepareFailView(String errorMessage);
}

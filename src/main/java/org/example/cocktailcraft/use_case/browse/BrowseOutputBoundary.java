package org.example.cocktailcraft.use_case.browse;

public interface BrowseOutputBoundary {
    void prepareSuccessView(BrowseOutputData outputData);

    void prepareFailView(String message);
}

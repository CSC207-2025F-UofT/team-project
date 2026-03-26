package use_case.searchLocation;

public interface SearchOutputBoundary {
    void prepareSuccessView(SearchOutputData outputData);
    void prepareFailView(String errorMessage);
}

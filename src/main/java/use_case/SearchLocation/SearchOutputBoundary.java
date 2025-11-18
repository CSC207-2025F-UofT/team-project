package use_case.SearchLocation;

public interface SearchOutputBoundary {
    void prepareSuccessView(SearchOutputData outputData);
    void prepareFailView(String errorMessage);
}

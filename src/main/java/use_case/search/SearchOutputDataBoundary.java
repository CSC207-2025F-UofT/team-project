package use_case.search;

public interface SearchOutputDataBoundary {
    void prepareSuccessView(SearchOutputData outputData);

    void prepareFailView(String errorMessage);
}

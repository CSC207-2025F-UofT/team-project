package use_case.filter_search;

public interface FilterSearchOutputBoundary {
    void prepareSuccessView(FilterSearchOutputData outputData);
    void prepareFailView(String errorMessage);
}

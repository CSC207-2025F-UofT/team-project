package use_case.filter_search;

public interface FilterSearchOutputBoundary {
    void present(FilterSearchOutputData outputData);
    void presentError(String errorMessage);
}

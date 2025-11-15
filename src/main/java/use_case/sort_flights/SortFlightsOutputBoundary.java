package use_case.sort_flights;

public interface SortFlightsOutputBoundary {
    void prepareSuccessView(SortFlightsOutputData sortFlightsOutputData);
    // We don't need a fail view for a simple sort.
}

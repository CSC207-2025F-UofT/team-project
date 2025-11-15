package use_case.find_flight;

public interface FindFlightOutputBoundary {

    void prepareSuccessView(FindFlightOutputData outputData);

    void prepareFailView(String errorMessage);
}
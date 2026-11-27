package use_case.playcard;

public interface PlayCardOutputBoundary {

    void prepareSuccessView(PlayCardOutputData outputData);

    void prepareFailView(String errorMessage);
}

package use_case.homescreen;

public interface HomescreenOutputBoundary {
    void prepareSuccessView(HomescreenOutputData outputData);
    void prepareFailView(String error);
}
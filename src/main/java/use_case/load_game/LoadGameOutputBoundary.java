package use_case.load_game;

public interface LoadGameOutputBoundary {
    void prepareLoadSuccessView(LoadGameOutputData output);

    void prepareFailView(String message);
}

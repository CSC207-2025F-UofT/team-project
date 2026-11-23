package use_case.load_scene;

public interface LoadSceneOutputBoundary {
    void prepareSuccessView(LoadSceneOutputData output);

    void prepareFailView(String error);
}

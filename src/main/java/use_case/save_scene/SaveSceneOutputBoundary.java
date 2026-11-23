package use_case.save_scene;

public interface SaveSceneOutputBoundary {
    void prepareSuccessView(SaveSceneOutputData output);

    void prepareOverwriteView(SaveSceneOutputData output);

    void prepareFailView(String error);
}

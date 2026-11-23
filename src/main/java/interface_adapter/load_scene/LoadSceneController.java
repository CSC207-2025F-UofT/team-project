package interface_adapter.load_scene;

import use_case.load_scene.LoadSceneInputBoundary;
import use_case.load_scene.LoadSceneInputData;

import java.nio.file.Path;

public final class LoadSceneController {
    private final LoadSceneInputBoundary interactor;

    public LoadSceneController(LoadSceneInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(Path path) {
        Path name = path.getFileName();
        interactor.execute(new LoadSceneInputData(path.toString(), name == null ? "" : name.toString()));
    }
}

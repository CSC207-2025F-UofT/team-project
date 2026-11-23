package interface_adapter.save_scene;

import use_case.save_scene.SaveSceneInputBoundary;
import use_case.save_scene.SaveSceneInputData;

import java.nio.file.Path;

public final class SaveSceneController {
    private final SaveSceneInputBoundary interactor;

    public SaveSceneController(SaveSceneInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(Path path) {
        execute(path, false);
    }

    public void execute(Path path, boolean overwrite) {
        Path name = path.getFileName();
        interactor.execute(new SaveSceneInputData(path.toString(), name == null ? "" : name.toString(), overwrite));
    }
}

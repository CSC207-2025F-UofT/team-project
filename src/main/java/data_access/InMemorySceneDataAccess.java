package data_access;

import entity.Scene;
import entity.SceneSnapshot;
import use_case.simulation.SimulationDataAccessInterface;

import java.util.Objects;

/** The single scene that all use cases in the running app share. */
public final class InMemorySceneDataAccess implements SimulationDataAccessInterface {
    private Scene currentScene;
    private SceneSnapshot startSnapshot;

    public InMemorySceneDataAccess(Scene initialScene) {
        replaceCurrentScene(initialScene);
    }

    @Override
    public Scene getCurrentScene() {
        return currentScene;
    }

    /** Also forgets the Play snapshot, since it belonged to the old scene. */
    public void replaceCurrentScene(Scene scene) {
        currentScene = Objects.requireNonNull(scene, "scene");
        startSnapshot = null;
    }

    @Override
    public SceneSnapshot getStartSnapshot() {
        return startSnapshot;
    }

    @Override
    public void setStartSnapshot(SceneSnapshot snapshot) {
        startSnapshot = snapshot;
    }
}

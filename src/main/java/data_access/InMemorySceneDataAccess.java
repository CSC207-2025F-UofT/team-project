package data_access;

import entity.Scene;

import java.util.Objects;

/** The single scene that all use cases in the running app share. */
public final class InMemorySceneDataAccess {
    private Scene currentScene;

    public InMemorySceneDataAccess(Scene initialScene) {
        replaceCurrentScene(initialScene);
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void replaceCurrentScene(Scene scene) {
        currentScene = Objects.requireNonNull(scene, "scene");
    }
}

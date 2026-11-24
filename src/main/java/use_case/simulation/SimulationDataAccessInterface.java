package use_case.simulation;

import entity.Scene;
import entity.SceneSnapshot;

/** The current scene, plus the snapshot taken when Play was pressed so Stop can go back to it. */
public interface SimulationDataAccessInterface {
    Scene getCurrentScene();

    /** Null when nothing has been played since the last stop or load. */
    SceneSnapshot getStartSnapshot();

    void setStartSnapshot(SceneSnapshot snapshot);
}

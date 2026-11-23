package use_case;

import entity.Scene;

public interface DropObjectDataAccessInterface {

    Scene getCurrentScene();

    /** Returns whether the current scene's terrain contains this world position. */
    boolean isInsideTerrain(double x, double y);

    /** Returns the current terrain height at an in-bounds world position. */
    double getTerrainHeightAt(double x, double y);

    void saveCurrentScene(Scene scene);
}

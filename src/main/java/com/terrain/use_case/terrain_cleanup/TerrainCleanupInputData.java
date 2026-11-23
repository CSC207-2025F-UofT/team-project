package com.terrain.use_case.terrain_cleanup;

/**
 * The thresholds supplied for the cleanup pass (from the user or the world settings).
 * The terrain itself is not passed in; the interactor reads it through the data access interface.
 */
public class TerrainCleanupInputData {
    private final double seaLevel;
    private final double treeLine; // TODO decide whether this is kept
    private final double snowLine; // This is most likely to be kept, cause it'll be the elevation after which we'll start to add snow

    public TerrainCleanupInputData(double seaLevel, double treeLine, double snowLine) {
        this.seaLevel = seaLevel;
        this.treeLine = treeLine;
        this.snowLine = snowLine;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public double getTreeLine() {
        return treeLine;
    }

    public double getSnowLine() {
        return snowLine;
    }
}

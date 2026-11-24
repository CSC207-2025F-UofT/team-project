package com.terrain.use_case.terrain_cleanup;

/**
 * Input boundary for the terrain cleanup use case. 
 */
public interface TerrainCleanupInputBoundary {

    /**
     * Executes the terrain cleanup use case.
     *
     * @param inputData the input data TODO decide what this specifically includes
     */
    void execute(TerrainCleanupInputData inputData);
}

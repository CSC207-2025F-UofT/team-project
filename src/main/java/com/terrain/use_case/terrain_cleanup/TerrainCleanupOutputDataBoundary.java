package com.terrain.use_case.terrain_cleanup;

/**
 * Output boundary for the terrain cleanup use case, implemented by the presenter.
 */
public interface TerrainCleanupOutputDataBoundary {

    /**
     * Called when the terrain was cleaned up successfully.
     *
     * @param outputData the final tile types and a summary of what changed
     */
    void prepareSuccessView(TerrainCleanupOutputData outputData);

    /**
     * Called when the cleanup could not run (no terrain, or invalid thresholds).
     *
     * @param errorMessage a message explaining why
     */
    void prepareFailView(String errorMessage);
}

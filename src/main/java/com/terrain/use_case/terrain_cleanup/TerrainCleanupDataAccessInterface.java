package com.terrain.use_case.terrain_cleanup;

import com.terrain.entity.model.TerrainMap;

/**
 * What the terrain cleanup use case needs from storage. Implemented in data_access.
 */
public interface TerrainCleanupDataAccessInterface {

    /** @return true if a terrain has been generated and can be cleaned up */
    boolean hasTerrain();

    /** @return the current terrain; only call this when hasTerrain() is true */
    TerrainMap getTerrain();

    /**
     * Saves the cleaned terrain. Each tile keeps its original type and now also has a final type.
     *
     * @param map the terrain, with every tile's final type set
     */
    void saveCleanedTerrain(TerrainMap map);
}

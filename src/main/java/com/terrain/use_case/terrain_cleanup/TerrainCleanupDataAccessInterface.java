package com.terrain.use_case.terrain_cleanup;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.TileType;

/**
 * What the terrain cleanup use case needs from storage. Implemented in data_access.
 */
public interface TerrainCleanupDataAccessInterface {

    /** @return true if a terrain has been generated and can be cleaned up */
    boolean hasTerrain();

    /** @return the current terrain; only call this when hasTerrain() is true */
    TerrainMap getTerrain();

    /**
     * Saves the cleaned terrain together with the final type of each tile.
     * This may be edited to only give a new map (since finalTypes is technically redundant) though this is tbd
     * 
     * @param map        the terrain, after conflicting features have been removed
     * @param finalTypes the final type of each tile, indexed [x][y]
     */
    void saveCleanedTerrain(TerrainMap map, TileType[][] finalTypes);
}

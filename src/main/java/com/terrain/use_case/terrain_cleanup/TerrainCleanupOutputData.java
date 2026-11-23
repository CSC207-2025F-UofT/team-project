package com.terrain.use_case.terrain_cleanup;

import com.terrain.entity.model.TileType;

/**
 * Result of a successful cleanup: the final type of every tile, plus how many conflicts were resolved.
 */
public class TerrainCleanupOutputData {
    private final TileType[][] finalTypes;
    private final int treesRemovedFromWater; // Subject to change 
    private final int treesRemovedAboveTreeLine; // Subject to change
    // May also consider outputing an actual TerrainMap object (TBD)

    public TerrainCleanupOutputData(TileType[][] finalTypes, int treesRemovedFromWater,
                                    int treesRemovedAboveTreeLine) {
        this.finalTypes = finalTypes;
        this.treesRemovedFromWater = treesRemovedFromWater;
        this.treesRemovedAboveTreeLine = treesRemovedAboveTreeLine;
    }

    /** Indexed [x][y], matching TerrainMap. */
    public TileType[][] getFinalTypes() {
        return finalTypes;
    }

    public int getTreesRemovedFromWater() {
        return treesRemovedFromWater;
    }

    public int getTreesRemovedAboveTreeLine() {
        return treesRemovedAboveTreeLine;
    }
}

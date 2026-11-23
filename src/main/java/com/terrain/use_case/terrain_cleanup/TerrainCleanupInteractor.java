package com.terrain.use_case.terrain_cleanup;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.TerrainRules;
import com.terrain.entity.model.Tile;
import com.terrain.entity.model.TileType;

/**
 * Finalizes the current terrain: gives every tile one final type and removes features that
 * break the realism rules (trees in water, trees above the tree line).
 */
public class TerrainCleanupInteractor implements TerrainCleanupInputBoundary {
    private final TerrainCleanupDataAccessInterface dataAccess;
    private final TerrainCleanupOutputDataBoundary presenter;

    public TerrainCleanupInteractor(TerrainCleanupDataAccessInterface dataAccess,
                                    TerrainCleanupOutputDataBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(TerrainCleanupInputData inputData) {
        if (!dataAccess.hasTerrain()) {
            presenter.prepareFailView("There is no terrain to finalize. Generate a terrain first.");
            return;
        }
        if (inputData.getTreeLine() <= inputData.getSeaLevel()) { // If the user sets the treeline too low (not specific placement of trees)
            presenter.prepareFailView("The tree line must be above sea level.");
            return;
        }
        if (inputData.getSnowLine() < inputData.getTreeLine()) { // If the user sets the snowline too low (not specific placement of trees or snow)
            presenter.prepareFailView("The snow line cannot be below the tree line.");
            return;
        }

        TerrainRules rules = new TerrainRules(
                inputData.getSeaLevel(), inputData.getTreeLine(), inputData.getSnowLine());
        TerrainMap map = dataAccess.getTerrain();
        TileType[][] finalTypes = new TileType[map.getWidth()][map.getHeight()];
        int treesRemovedFromWater = 0;
        int treesRemovedAboveTreeLine = 0;

        for (int x = 0; x < map.getWidth(); x++) { // Most subject to change under new logic
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                if (tile.getTreeDensity() > 0 && !rules.allowsTrees(tile)) {
                    if (rules.isUnderwater(tile)) {
                        treesRemovedFromWater++;
                    } else if (rules.isAboveTreeLine(tile)) {
                        treesRemovedAboveTreeLine++;
                    }
                    tile.setTreeDensity(0);
                }
                // Classify after removing trees, so the type matches what is actually on the tile.
                finalTypes[x][y] = rules.classify(tile);
            }
        }

        dataAccess.saveCleanedTerrain(map, finalTypes);
        presenter.prepareSuccessView(
                new TerrainCleanupOutputData(finalTypes, treesRemovedFromWater, treesRemovedAboveTreeLine));
    }
}

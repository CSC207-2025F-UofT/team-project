package data_access;

import entity.Terrain;
import use_case.scene.SceneViewData.HeightGrid;

/** Converts between a scene's terrain entity and its height-grid data. */
public final class SceneTerrainMapper implements FileSceneDataAccess.TerrainMapper {
    @Override
    public HeightGrid toHeightGrid(Terrain terrain) {
        if (terrain == null) {
            return null;
        }
        return new HeightGrid(terrain.getName(), terrain.getCellSize(), terrain.getHeights());
    }

    @Override
    public Terrain toTerrain(HeightGrid grid) {
        double[][] heights = new double[grid.getRows()][grid.getCols()];
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                heights[row][col] = grid.getHeight(row, col);
            }
        }
        return new Terrain(grid.getName(), grid.getCellSize(), heights);
    }
}

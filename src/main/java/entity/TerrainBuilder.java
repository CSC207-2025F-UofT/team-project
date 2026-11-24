package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Builds a Terrain from a ground level plus a list of TerrainFeatures.
// Feature positions and sizes are in grid cells, so the same features give the same shape
// at any cell size. Overlapping features add together.

public final class TerrainBuilder {

    public static final int DEFAULT_GRID_SIZE = 48;
    public static final double DEFAULT_CELL_SIZE = 2.0;

    private final String name;
    private final List<TerrainFeature> features = new ArrayList<>();
    private double ground;
    private int rows = DEFAULT_GRID_SIZE;
    private int cols = DEFAULT_GRID_SIZE;
    private double cellSize = DEFAULT_CELL_SIZE;

    public TerrainBuilder(String name) {
        this.name = name;
    }

    public TerrainBuilder ground(double ground) {
        this.ground = ground;
        return this;
    }

    public TerrainBuilder gridSize(int rows, int cols) {
        if (rows < 2 || cols < 2) {
            throw new IllegalArgumentException("grid must be at least 2 x 2");
        }
        this.rows = rows;
        this.cols = cols;
        return this;
    }

    public TerrainBuilder cellSize(double cellSize) {
        if (cellSize <= 0) {
            throw new IllegalArgumentException("cellSize must be positive");
        }
        this.cellSize = cellSize;
        return this;
    }

    public TerrainBuilder addFeature(TerrainFeature feature) {
        features.add(Objects.requireNonNull(feature, "feature"));
        return this;
    }

    public TerrainBuilder addFeatures(List<TerrainFeature> features) {
        for (TerrainFeature feature : features) {
            addFeature(feature);
        }
        return this;
    }

    public Terrain build() {
        double[][] heights = new double[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double height = ground;
                for (TerrainFeature feature : features) {
                    height += feature.heightAt(col, row);
                }
                heights[row][col] = height;
            }
        }
        return new Terrain(name, cellSize, heights);
    }
}

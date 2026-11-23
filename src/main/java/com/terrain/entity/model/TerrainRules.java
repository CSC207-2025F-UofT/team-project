package com.terrain.entity.model;

/**
 * Realism rules for a finished terrain: which type a tile should end up as, and where trees may grow.
 * Elevation bands, from low to high: WATER, SAND, GRASS or FOREST, STONE (above the tree line),
 * SNOW (above the snow line).
 * TO BE EDITED A GOOD AMOUNT
 * Note that if it is decided to be multiple tiletypes per tile, final typing will need to resolve between them (so have that logic)
 */
public class TerrainRules {
    /** How far above sea level the sand extends. */
    public static final double SAND_HEIGHT = 2.0; // TODO decide if this should be a parameter instead
    /** Minimum tree density for a tile to count as forest rather than grass. */
    public static final double FOREST_DENSITY = 0.3; // TODO decide whether density in relation to this decides tiletype probabilistically

    private final double seaLevel;
    private final double treeLine;
    private final double snowLine;

    public TerrainRules(double seaLevel, double treeLine, double snowLine) {
        this.seaLevel = seaLevel;
        this.treeLine = treeLine;
        this.snowLine = snowLine;
    }

    public boolean isUnderwater(Tile tile) {
        return tile.getElevation() < seaLevel;
    }

    public boolean isAboveTreeLine(Tile tile) {
        return tile.getElevation() >= treeLine;
    }

    /** Trees may only grow on dry land between the sand and the tree line. */
    public boolean allowsTrees(Tile tile) {
        double elevation = tile.getElevation();
        return elevation >= seaLevel + SAND_HEIGHT && elevation < treeLine;
    }

    /** 
     * The single final type of a tile, given its elevation and tree density. 
     * Subject to change
    */
    public TileType classify(Tile tile) {
        double elevation = tile.getElevation();
        if (elevation < seaLevel) {
            return TileType.WATER;
        }
        if (elevation >= snowLine) {
            return TileType.SNOW;
        }
        if (elevation >= treeLine) {
            return TileType.STONE;
        }
        if (elevation < seaLevel + SAND_HEIGHT) {
            return TileType.SAND;
        }
        if (tile.getTreeDensity() >= FOREST_DENSITY) {
            return TileType.FOREST;
        }
        return TileType.GRASS;
    }
}

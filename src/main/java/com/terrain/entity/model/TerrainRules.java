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
    public static final double SAND_HEIGHT = 2.0; // TODO decide if this should be a parameter instead (also need to make sure this stays adjacent to river)

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

    /** A tile has trees if a feature (e.g. a forest) has marked it as FOREST. */
    public boolean hasTrees(Tile tile) {
        return tile.getType() == TileType.FOREST;
    }

    /** Trees may only grow on dry land between the sand and the tree line. */
    public boolean allowsTrees(Tile tile) {
        double elevation = tile.getElevation();
        return elevation >= seaLevel + SAND_HEIGHT && elevation < treeLine;
    }

    /** 
     * The single final type of a tile, given its elevation and whether a feature placed trees on it.
     * Elevation bands take priority, so a FOREST tile outside the tree band is reclassified.
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
        if (hasTrees(tile)) {
            return TileType.FOREST;
        }
        return TileType.GRASS; // TODO Else, should return same? Potential that other default's can exist
    }
}

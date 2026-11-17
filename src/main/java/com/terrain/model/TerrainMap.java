package com.terrain.model;

public class TerrainMap {
    private final int width;
    private final int height;
    private final Tile[][] tiles;

    public TerrainMap(int width, int height) {
        this.width = width;
        this.height = height;

        // Initialize the tiles array with default Tile objects
        tiles = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile();
            }
        }
    }

    // This can totally return null, if a feature requests a tile out of bounds; Need to decide if we want to handle
    // that here or in the feature.
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }
}

// TODO: There will be different types of tiles, such as water, land, mountain, etc.
// We need to decide whether this is a property of a given tile, or something derived from other tile properties at some
// point after the map is mostly generated. For example, we have an "elevation" property for each tile, and we could
// derive the type of tile from that height, e.g., water if below sea level, land if above sea level, mountain if above
// a certain height. We could also have moisture, sand, etc. properties, and derive the tile type from those as well.
package com.terrain.entity.model;

public class TerrainMap {
    private final int width;
    private final int height;
    private double maxElevation;
    private double minElevation;
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

    public boolean tileInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Tile getTile(int x, int y) {
        if(!tileInBounds(x, y)) {
            throw new IndexOutOfBoundsException("Tile coordinates out of bounds: (" + x + ", " + y + ")");
        }
        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // finds the min and max elevation values in the map, for use in color mapping
    public void getElevationBounds() {
        maxElevation = 0;
        minElevation = 0;
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double elevation = tiles[x][y].getElevation();
                if (x == 0 && y == 0) {
                    maxElevation = elevation;
                    minElevation = elevation;
                }
                if(elevation > maxElevation) {
                    maxElevation = elevation;
                }
                if(elevation < minElevation) {
                    minElevation = elevation;
                }
            }
        }
    }

    public double getMaxElevation() {
        return maxElevation;
    }

    public double getMinElevation() {
        return minElevation;
    }
}

// TODO: There will be different types of tiles, such as water, land, mountain, etc.
// We need to decide whether this is a property of a given tile, or something derived from other tile properties at some
// point after the map is mostly generated. For example, we have an "elevation" property for each tile, and we could
// derive the type of tile from that height, e.g., water if below sea level, land if above sea level, mountain if above
// a certain height. We could also have moisture, sand, etc. properties, and derive the tile type from those as well.
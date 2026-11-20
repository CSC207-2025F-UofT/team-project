package entity;

/** An immutable rectangular grid of TileType implementations with tile and pixel lookups. */
public final class TileMap {
    private final TileType[][] tiles;
    private final int tileSize;

    /**
     * Copies a terrain grid so that changes to the supplied arrays cannot change this map.
     * Terrain objects are shared and must obey TileType's immutability contract.
     *
     * @param tiles terrain arranged as rows, then columns
     * @param tileSize width and height of each square tile in pixels
     * @throws IllegalArgumentException if tileSize is not positive, the grid is null, empty,
     *                                  uneven or contains nulls, or pixel dimensions exceed an int
     */
    public TileMap(TileType[][] tiles, int tileSize) {
        if (tileSize <= 0) {
            throw new IllegalArgumentException("Tile size must be positive.");
        }
        if (tiles == null || tiles.length == 0) {
            throw new IllegalArgumentException("A tile map must contain at least one row.");
        }
        if (tiles[0] == null || tiles[0].length == 0) {
            throw new IllegalArgumentException("A tile map must contain at least one column.");
        }

        int columns = tiles[0].length;
        if ((long) columns * tileSize > Integer.MAX_VALUE
                || (long) tiles.length * tileSize > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Map pixel dimensions exceed the supported range.");
        }

        this.tileSize = tileSize;
        this.tiles = new TileType[tiles.length][columns];
        for (int row = 0; row < tiles.length; row++) {
            if (tiles[row] == null || tiles[row].length != columns) {
                throw new IllegalArgumentException("Row " + row + " must contain " + columns + " tiles.");
            }
            for (int col = 0; col < columns; col++) {
                if (tiles[row][col] == null) {
                    throw new IllegalArgumentException("Tile at column " + col + ", row " + row + " is null.");
                }
                this.tiles[row][col] = tiles[row][col];
            }
        }
    }

    /** Returns the number of columns in the grid. */
    public int getColumnCount() {
        return tiles[0].length;
    }

    /** Returns the number of rows in the grid. */
    public int getRowCount() {
        return tiles.length;
    }

    /** Returns the width and height of a tile in pixels. */
    public int getTileSize() {
        return tileSize;
    }

    /** Returns the map width in pixels. */
    public int getWidthInPixels() {
        return getColumnCount() * tileSize;
    }

    /** Returns the map height in pixels. */
    public int getHeightInPixels() {
        return getRowCount() * tileSize;
    }

    /**
     * Gets a tile using zero-based column and row indices.
     *
     * @param col column index, increasing to the right
     * @param row row index, increasing downward
     * @return the terrain at that grid position
     * @throws IndexOutOfBoundsException if either index is outside the grid
     */
    public TileType getTile(int col, int row) {
        if (col < 0 || col >= getColumnCount() || row < 0 || row >= getRowCount()) {
            throw new IndexOutOfBoundsException("Tile coordinates out of bounds: (" + col + ", " + row + ")");
        }
        return tiles[row][col];
    }

    /**
     * Finds the tile containing a pixel position measured from the map's top-left corner.
     * A tile includes its top and left edges, but excludes its bottom and right edges.
     *
     * @param x horizontal position in pixels
     * @param y vertical position in pixels
     * @return the terrain containing the position
     * @throws IndexOutOfBoundsException if the position is outside the map or is not finite
     */
    public TileType tileAtPixel(double x, double y) {
        if (!containsPixel(x, y)) {
            throw new IndexOutOfBoundsException("Pixel coordinates out of bounds: (" + x + ", " + y + ")");
        }
        return getTile((int) (x / tileSize), (int) (y / tileSize));
    }

    /**
     * Checks terrain walkability at a pixel position; this does not move any entity.
     *
     * @param x horizontal position in pixels
     * @param y vertical position in pixels
     * @return the terrain's walking rule, or false for positions outside the map
     *         and non-finite coordinates
     */
    public boolean isWalkable(double x, double y) {
        return containsPixel(x, y) && tileAtPixel(x, y).isWalkable();
    }

    private boolean containsPixel(double x, double y) {
        return x >= 0 && x < getWidthInPixels() && y >= 0 && y < getHeightInPixels();
    }
}

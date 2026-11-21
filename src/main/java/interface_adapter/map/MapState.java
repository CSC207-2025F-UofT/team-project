package interface_adapter.map;

/** An immutable map display snapshot containing only tile IDs, dimensions and error text. */
public final class MapState {
    private final int[][] tileIds;
    private final int tileSize;
    private final String errorMessage;

    /** Creates the initial state, with no map and no loading error. */
    public MapState() {
        this(new int[0][0], 0, "");
    }

    /**
     * Copies a validated map snapshot, or an empty grid with tileSize 0 before the first load.
     * The error message is empty when there is no loading error.
     */
    public MapState(int[][] tileIds, int tileSize, String errorMessage) {
        this.tileIds = copyGrid(tileIds);
        this.tileSize = tileSize;
        this.errorMessage = errorMessage;
    }

    public boolean hasMap() {
        return tileIds.length > 0;
    }

    /** Returns a fresh copy so callers cannot change this snapshot. */
    public int[][] getTileIds() {
        return copyGrid(tileIds);
    }

    /** Returns a terrain ID at a valid, zero-based column and row. */
    public int getTileId(int col, int row) {
        return tileIds[row][col];
    }

    public int getColumnCount() {
        if (!hasMap()) {
            return 0;
        }
        return tileIds[0].length;
    }

    public int getRowCount() {
        return tileIds.length;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getWidthInPixels() {
        return getColumnCount() * tileSize;
    }

    public int getHeightInPixels() {
        return getRowCount() * tileSize;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private int[][] copyGrid(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int row = 0; row < source.length; row++) {
            copy[row] = source[row].clone();
        }
        return copy;
    }
}

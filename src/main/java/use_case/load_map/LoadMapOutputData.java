package use_case.load_map;

/** An immutable snapshot of terrain IDs and tile size, with no entity references. */
public final class LoadMapOutputData {
    private final int[][] tileIds;
    private final int tileSize;

    /**
     * Copies the IDs of an already validated map, arranged as rows then columns.
     * The grid must be non-null, non-empty and rectangular, and tileSize must be positive.
     */
    public LoadMapOutputData(int[][] tileIds, int tileSize) {
        this.tileIds = copyGrid(tileIds);
        this.tileSize = tileSize;
    }

    /** Returns a fresh copy so callers cannot change this snapshot. */
    public int[][] getTileIds() {
        return copyGrid(tileIds);
    }

    public int getTileSize() {
        return tileSize;
    }

    private int[][] copyGrid(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int row = 0; row < source.length; row++) {
            copy[row] = source[row].clone();
        }
        return copy;
    }
}

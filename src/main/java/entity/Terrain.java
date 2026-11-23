package entity;

// An immutable heightmap terrain.
// heights[row][col] is the ground height in metres at grid point (col, row).
// x runs along columns and y along rows, both in metres: x = col * cellSize.
// Points outside the grid are clamped to the nearest edge.

public final class Terrain {

    private final String name;
    private final int rows;
    private final int cols;
    private final double cellSize;
    private final double[][] heights;

    public Terrain(String name, double cellSize, double[][] heights) {
        if (cellSize <= 0) {
            throw new IllegalArgumentException("cellSize must be positive");
        }
        if (heights == null || heights.length < 2 || heights[0] == null || heights[0].length < 2) {
            throw new IllegalArgumentException("heights must be at least 2 x 2");
        }
        this.name = name;
        this.rows = heights.length;
        this.cols = heights[0].length;
        this.cellSize = cellSize;
        this.heights = new double[rows][];
        for (int row = 0; row < rows; row++) {
            if (heights[row] == null || heights[row].length != cols) {
                throw new IllegalArgumentException("every row of heights must have the same length");
            }
            this.heights[row] = heights[row].clone();
        }
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double getCellSize() {
        return cellSize;
    }

    /** Returns the stored height at a grid point. */
    public double getHeight(int row, int col) {
        return heights[row][col];
    }

    /** Returns a copy of the height grid, so callers cannot change this terrain. */
    public double[][] getHeights() {
        double[][] copy = new double[rows][];
        for (int row = 0; row < rows; row++) {
            copy[row] = heights[row].clone();
        }
        return copy;
    }

    /** Size of the terrain along x, in metres. */
    public double width() {
        return (cols - 1) * cellSize;
    }

    /** Size of the terrain along y, in metres. */
    public double depth() {
        return (rows - 1) * cellSize;
    }

    public boolean isInside(double x, double y) {
        return x >= 0 && x <= width() && y >= 0 && y <= depth();
    }

    /** Ground height at (x, y), blended from the four nearest grid points. */
    public double heightAt(double x, double y) {
        Cell cell = cellAt(x, y);
        double top = lerp(cell.h00, cell.h10, cell.fx);
        double bottom = lerp(cell.h01, cell.h11, cell.fx);
        return lerp(top, bottom, cell.fy);
    }

    /**
     * Slope at (x, y) as a vector in the xy plane pointing uphill.
     * Its length is the rise per metre, so steeper ground gives a longer vector.
     */
    public Vector3D slopeAt(double x, double y) {
        Cell cell = cellAt(x, y);
        double dx = lerp(cell.h10 - cell.h00, cell.h11 - cell.h01, cell.fy) / cellSize;
        double dy = lerp(cell.h01 - cell.h00, cell.h11 - cell.h10, cell.fx) / cellSize;
        return new Vector3D(dx, dy, 0);
    }

    @Override
    public String toString() {
        return "Terrain{name=" + name + ", rows=" + rows + ", cols=" + cols
                + ", cellSize=" + cellSize + "}";
    }

    // Finds the grid cell holding (x, y) after clamping, and how far across it the point is.
    private Cell cellAt(double x, double y) {
        double gx = clamp(x / cellSize, 0, cols - 1);
        double gy = clamp(y / cellSize, 0, rows - 1);
        int col = Math.min((int) gx, cols - 2);
        int row = Math.min((int) gy, rows - 2);
        return new Cell(
                heights[row][col], heights[row][col + 1],
                heights[row + 1][col], heights[row + 1][col + 1],
                gx - col, gy - row);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    // h00 is (col, row), h10 is (col + 1, row), h01 is (col, row + 1), h11 is (col + 1, row + 1).
    private static final class Cell {
        private final double h00;
        private final double h10;
        private final double h01;
        private final double h11;
        private final double fx;
        private final double fy;

        private Cell(double h00, double h10, double h01, double h11, double fx, double fy) {
            this.h00 = h00;
            this.h10 = h10;
            this.h01 = h01;
            this.h11 = h11;
            this.fx = fx;
            this.fy = fy;
        }
    }
}

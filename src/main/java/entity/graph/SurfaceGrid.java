package entity.graph;

import entity.SurfaceFunction;

// z on an n x n grid over the x / y window, NaN where f is undefined
public final class SurfaceGrid {
    private final Viewport domain;
    private final double[][] z;   // z[i][j] = f(x_i, y_j)
    private final double zMin;
    private final double zMax;
    private final boolean empty;

    private SurfaceGrid(Viewport domain, double[][] z, double zMin, double zMax, boolean empty) {
        this.domain = domain;
        this.z = z;
        this.zMin = zMin;
        this.zMax = zMax;
        this.empty = empty;
    }

    public static SurfaceGrid sample(SurfaceFunction f, Viewport domain, int n) {
        if (n < 2) {
            throw new IllegalArgumentException("grid needs at least 2 points a side");
        }
        double[][] z = new double[n][n];
        double[] flat = new double[n * n];
        for (int i = 0; i < n; i++) {
            double x = domain.getXMin() + domain.width() * i / (n - 1);
            for (int j = 0; j < n; j++) {
                double y = domain.getYMin() + domain.height() * j / (n - 1);
                double v = f.evaluate(x, y);
                z[i][j] = Double.isFinite(v) ? v : Double.NaN;
                flat[i * n + j] = z[i][j];
            }
        }
        double[] range = AutoRange.window(flat);
        if (range == null) {
            return new SurfaceGrid(domain, z, -1, 1, true);
        }
        return new SurfaceGrid(domain, z, range[0], range[1], false);
    }

    public Viewport getDomain() {
        return domain;
    }

    public int size() {
        return z.length;
    }

    public double xAt(int i) {
        return domain.getXMin() + domain.width() * i / (z.length - 1);
    }

    public double yAt(int j) {
        return domain.getYMin() + domain.height() * j / (z.length - 1);
    }

    public double zAt(int i, int j) {
        return z[i][j];
    }

    public double[][] getZ() {
        double[][] copy = new double[z.length][];
        for (int i = 0; i < z.length; i++) {
            copy[i] = z[i].clone();
        }
        return copy;
    }

    public double getZMin() {
        return zMin;
    }

    public double getZMax() {
        return zMax;
    }

    // true when f was undefined on the whole window
    public boolean isEmpty() {
        return empty;
    }
}

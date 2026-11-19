package entity.phase;

import entity.graph.Viewport;

// the field sampled on a grid in the window, NaN arrows where it's undefined
public final class ArrowField {
    private final double[] hs;
    private final double[] vs;
    private final double[] dhs;
    private final double[] dvs;

    private ArrowField(double[] hs, double[] vs, double[] dhs, double[] dvs) {
        this.hs = hs;
        this.vs = vs;
        this.dhs = dhs;
        this.dvs = dvs;
    }

    // cols x rows arrows, each in the middle of its cell so none sit on the border
    public static ArrowField sample(DifferentialEquation eq, Viewport view, int cols, int rows) {
        int n = cols * rows;
        double[] hs = new double[n];
        double[] vs = new double[n];
        double[] dhs = new double[n];
        double[] dvs = new double[n];
        int k = 0;
        for (int r = 0; r < rows; r++) {
            double v = view.getYMin() + (r + 0.5) / rows * view.height();
            for (int c = 0; c < cols; c++) {
                double h = view.getXMin() + (c + 0.5) / cols * view.width();
                hs[k] = h;
                vs[k] = v;
                dhs[k] = eq.dh(h, v);
                dvs[k] = eq.dv(h, v);
                k++;
            }
        }
        return new ArrowField(hs, vs, dhs, dvs);
    }

    public int size() {
        return hs.length;
    }

    public double[] getHs() {
        return hs.clone();
    }

    public double[] getVs() {
        return vs.clone();
    }

    public double[] getDhs() {
        return dhs.clone();
    }

    public double[] getDvs() {
        return dvs.clone();
    }
}

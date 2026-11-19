package entity.phase;

import entity.graph.Curve;

// what's under the cursor: the point (snapped onto an equilibrium if close), its arrow, the path through it
public final class PhaseTracePoint {
    private final double h;
    private final double v;
    private final double dh;
    private final double dv;
    private final Equilibrium equilibrium;   // null = ordinary point
    private final Curve path;

    public PhaseTracePoint(double h, double v, double dh, double dv, Equilibrium equilibrium, Curve path) {
        this.h = h;
        this.v = v;
        this.dh = dh;
        this.dv = dv;
        this.equilibrium = equilibrium;
        this.path = path;
    }

    public double getH() {
        return h;
    }

    public double getV() {
        return v;
    }

    public double getDh() {
        return dh;
    }

    public double getDv() {
        return dv;
    }

    public Equilibrium getEquilibrium() {
        return equilibrium;
    }

    public Curve getPath() {
        return path;
    }
}

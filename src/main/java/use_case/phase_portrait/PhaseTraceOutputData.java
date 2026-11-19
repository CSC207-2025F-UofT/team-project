package use_case.phase_portrait;

import java.util.List;

// cursor readout: the point, the arrow there, the equilibrium it snapped to (or null), the path through it
public class PhaseTraceOutputData {
    private final double h;
    private final double v;
    private final double dh;
    private final double dv;
    private final boolean slopeField;
    private final EquilibriumOutputData equilibrium;
    private final List<double[][]> path;

    public PhaseTraceOutputData(double h, double v, double dh, double dv, boolean slopeField,
                                EquilibriumOutputData equilibrium, List<double[][]> path) {
        this.h = h;
        this.v = v;
        this.dh = dh;
        this.dv = dv;
        this.slopeField = slopeField;
        this.equilibrium = equilibrium;
        this.path = List.copyOf(path);
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

    public boolean isSlopeField() {
        return slopeField;
    }

    public EquilibriumOutputData getEquilibrium() {
        return equilibrium;
    }

    public List<double[][]> getPath() {
        return path;
    }
}

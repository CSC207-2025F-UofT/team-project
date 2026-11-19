package use_case.phase_portrait;

import entity.phase.Equilibrium;

// h is NaN for a line v = c
public class EquilibriumOutputData {
    private final double h;
    private final double v;
    private final Equilibrium.Type type;

    public EquilibriumOutputData(double h, double v, Equilibrium.Type type) {
        this.h = h;
        this.v = v;
        this.type = type;
    }

    public double getH() {
        return h;
    }

    public double getV() {
        return v;
    }

    public Equilibrium.Type getType() {
        return type;
    }

    public boolean isLine() {
        return type.isLine();
    }
}

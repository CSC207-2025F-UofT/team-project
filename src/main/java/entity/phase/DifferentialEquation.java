package entity.phase;

import java.util.function.DoubleBinaryOperator;

/**
 * An ODE turned into a plane vector field (h', v') = (dh(h, v), dv(h, v)).
 * Slope field: y' = f(x, y) -> (1, f). Phase plane: y'' = f(y, y') or a 2x2 system -> (f, g).
 */
public final class DifferentialEquation {
    public enum Kind { SLOPE_FIELD, PHASE_PLANE }

    private final String latex;
    private final Kind kind;
    private final String horizontalName;
    private final String verticalName;
    private final DoubleBinaryOperator dh;
    private final DoubleBinaryOperator dv;

    public DifferentialEquation(String latex, Kind kind, String horizontalName, String verticalName,
                                DoubleBinaryOperator dh, DoubleBinaryOperator dv) {
        this.latex = latex;
        this.kind = kind;
        this.horizontalName = horizontalName;
        this.verticalName = verticalName;
        this.dh = dh;
        this.dv = dv;
    }

    public String getLatex() {
        return latex;
    }

    public Kind getKind() {
        return kind;
    }

    // name as written: "x", "t", "\theta", "y'" ...
    public String getHorizontalName() {
        return horizontalName;
    }

    public String getVerticalName() {
        return verticalName;
    }

    // horizontal part of the arrow at (h, v), NaN where undefined
    public double dh(double h, double v) {
        return dh.applyAsDouble(h, v);
    }

    public double dv(double h, double v) {
        return dv.applyAsDouble(h, v);
    }
}

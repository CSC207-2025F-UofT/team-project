package entity;

import java.util.function.DoubleBinaryOperator;

// z = f(x, y), what the 3d graph draws, keeps the latex it came from
public class SurfaceFunction {
    private final String latex;
    private final DoubleBinaryOperator rule;

    public SurfaceFunction(String latex, DoubleBinaryOperator rule) {
        this.latex = latex;
        this.rule = rule;
    }

    public String getLatex() {
        return latex;
    }

    // NaN / infinity where it's undefined
    public double evaluate(double x, double y) {
        return rule.applyAsDouble(x, y);
    }
}

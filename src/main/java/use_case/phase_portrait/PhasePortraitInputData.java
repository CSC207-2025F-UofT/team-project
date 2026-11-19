package use_case.phase_portrait;

/** The ODE's LaTeX plus the window; a NaN y range means pick one from the x range. */
public class PhasePortraitInputData {
    public static final double DEFAULT_X_MIN = -10;
    public static final double DEFAULT_X_MAX = 10;
    public static final double AUTOMATIC = Double.NaN;

    private final String latex;
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;

    public PhasePortraitInputData(String latex) {
        this(latex, DEFAULT_X_MIN, DEFAULT_X_MAX, AUTOMATIC, AUTOMATIC);
    }

    public PhasePortraitInputData(String latex, double xMin, double xMax, double yMin, double yMax) {
        this.latex = latex;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public String getLatex() {
        return latex;
    }

    public double getXMin() {
        return xMin;
    }

    public double getXMax() {
        return xMax;
    }

    public boolean isYAutomatic() {
        return Double.isNaN(yMin) && Double.isNaN(yMax);
    }

    public double getYMin() {
        return yMin;
    }

    public double getYMax() {
        return yMax;
    }
}

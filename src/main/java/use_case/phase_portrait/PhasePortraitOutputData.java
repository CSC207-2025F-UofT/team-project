package use_case.phase_portrait;

import java.util.List;

// everything the card draws, plain numbers; arrows = {hs, vs, dhs, dvs}, curves = {xs, ys} pieces
public class PhasePortraitOutputData {
    private final String latex;
    private final boolean slopeField;
    private final String horizontalName;
    private final String verticalName;
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;
    private final double[][] arrows;
    private final List<EquilibriumOutputData> equilibria;
    private final List<double[][]> solutions;

    public PhasePortraitOutputData(String latex, boolean slopeField, String horizontalName, String verticalName,
                                   double xMin, double xMax, double yMin, double yMax, double[][] arrows,
                                   List<EquilibriumOutputData> equilibria, List<double[][]> solutions) {
        this.latex = latex;
        this.slopeField = slopeField;
        this.horizontalName = horizontalName;
        this.verticalName = verticalName;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.arrows = arrows;
        this.equilibria = List.copyOf(equilibria);
        this.solutions = List.copyOf(solutions);
    }

    public String getLatex() {
        return latex;
    }

    public boolean isSlopeField() {
        return slopeField;
    }

    public String getHorizontalName() {
        return horizontalName;
    }

    public String getVerticalName() {
        return verticalName;
    }

    public double getXMin() {
        return xMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getYMin() {
        return yMin;
    }

    public double getYMax() {
        return yMax;
    }

    public double[][] getArrows() {
        return arrows;
    }

    public List<EquilibriumOutputData> getEquilibria() {
        return equilibria;
    }

    public List<double[][]> getSolutions() {
        return solutions;
    }
}

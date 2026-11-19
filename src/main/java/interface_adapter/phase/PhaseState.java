package interface_adapter.phase;

import interface_adapter.graph.GraphTick;

import java.util.Collections;
import java.util.List;

// what the phase card shows; copied, changed, handed to the view model
public class PhaseState {
    public enum Kind { NONE, SLOPE_FIELD, PHASE_PLANE }

    private Kind kind = Kind.NONE;
    private String latex;
    private String error;
    private long revision;   // up on every brand new portrait, the card drops its pin on it
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private String horizontalLabel = "x";
    private String verticalLabel = "y";
    private double[][] arrows = new double[4][0];
    private List<PhaseMarker> markers = Collections.emptyList();
    private List<double[][]> solutions = Collections.emptyList();
    private List<GraphTick> xTicks = Collections.emptyList();
    private List<GraphTick> yTicks = Collections.emptyList();
    private PhaseReadout trace;

    public PhaseState() {
    }

    public PhaseState(PhaseState copy) {
        kind = copy.kind;
        latex = copy.latex;
        error = copy.error;
        revision = copy.revision;
        xMin = copy.xMin;
        xMax = copy.xMax;
        yMin = copy.yMin;
        yMax = copy.yMax;
        horizontalLabel = copy.horizontalLabel;
        verticalLabel = copy.verticalLabel;
        arrows = copy.arrows;
        markers = copy.markers;
        solutions = copy.solutions;
        xTicks = copy.xTicks;
        yTicks = copy.yTicks;
        trace = copy.trace;
    }

    public boolean hasPortrait() {
        return kind != Kind.NONE;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public String getLatex() {
        return latex;
    }

    public void setLatex(String latex) {
        this.latex = latex;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public void setWindow(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
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

    public String getHorizontalLabel() {
        return horizontalLabel;
    }

    public String getVerticalLabel() {
        return verticalLabel;
    }

    public void setLabels(String horizontal, String vertical) {
        this.horizontalLabel = horizontal;
        this.verticalLabel = vertical;
    }

    // {hs, vs, dhs, dvs}
    public double[][] getArrows() {
        return arrows;
    }

    public void setArrows(double[][] arrows) {
        this.arrows = arrows;
    }

    public List<PhaseMarker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<PhaseMarker> markers) {
        this.markers = List.copyOf(markers);
    }

    public List<double[][]> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<double[][]> solutions) {
        this.solutions = List.copyOf(solutions);
    }

    public List<GraphTick> getXTicks() {
        return xTicks;
    }

    public List<GraphTick> getYTicks() {
        return yTicks;
    }

    public void setTicks(List<GraphTick> xTicks, List<GraphTick> yTicks) {
        this.xTicks = List.copyOf(xTicks);
        this.yTicks = List.copyOf(yTicks);
    }

    public PhaseReadout getTrace() {
        return trace;
    }

    public void setTrace(PhaseReadout trace) {
        this.trace = trace;
    }
}

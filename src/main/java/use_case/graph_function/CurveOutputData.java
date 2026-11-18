package use_case.graph_function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// a 2d graph ready to draw: the window, the line pieces in world units, the key points
public class CurveOutputData {
    private final String latex;
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;
    private final List<double[][]> pieces;   // each one {xs, ys}
    private final List<KeyPointOutputData> keyPoints;

    public CurveOutputData(String latex, double xMin, double xMax, double yMin, double yMax,
                           List<double[][]> pieces, List<KeyPointOutputData> keyPoints) {
        this.latex = latex;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.pieces = Collections.unmodifiableList(new ArrayList<>(pieces));
        this.keyPoints = Collections.unmodifiableList(new ArrayList<>(keyPoints));
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

    public double getYMin() {
        return yMin;
    }

    public double getYMax() {
        return yMax;
    }

    public List<double[][]> getPieces() {
        return pieces;
    }

    public List<KeyPointOutputData> getKeyPoints() {
        return keyPoints;
    }
}

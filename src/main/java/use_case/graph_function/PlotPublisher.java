package use_case.graph_function;

import entity.graph.Curve;
import entity.graph.CurvePlot;
import entity.graph.KeyPoint;
import entity.graph.Plot;
import entity.graph.PlotVisitor;
import entity.graph.SurfaceGrid;
import entity.graph.SurfacePlot;
import entity.graph.TracePoint;
import entity.graph.Viewport;

import java.util.ArrayList;
import java.util.List;

// plot -> plain output data -> presenter, shared by the graph + explore use cases
public class PlotPublisher {
    private final GraphFunctionOutputBoundary presenter;

    public PlotPublisher(GraphFunctionOutputBoundary presenter) {
        this.presenter = presenter;
    }

    public void publish(Plot plot, boolean fresh) {
        plot.accept(new PlotVisitor<Void>() {
            @Override
            public Void visitCurve(CurvePlot curve) {
                presenter.prepareCurveView(curveData(curve), fresh);
                return null;
            }

            @Override
            public Void visitSurface(SurfacePlot surface) {
                presenter.prepareSurfaceView(surfaceData(surface), fresh);
                return null;
            }
        });
    }

    public void publishTrace(TracePoint point) {
        presenter.prepareTraceView(point == null ? null
                : new TraceOutputData(point.getX(), point.getY(), point.getZ(), point.getKind()));
    }

    static CurveOutputData curveData(CurvePlot plot) {
        Viewport v = plot.getViewport();
        List<double[][]> pieces = new ArrayList<>();
        for (Curve.Piece piece : plot.getCurve().getPieces()) {
            pieces.add(new double[][]{piece.getXs(), piece.getYs()});
        }
        List<KeyPointOutputData> keys = new ArrayList<>();
        for (KeyPoint k : plot.getKeyPoints()) {
            keys.add(new KeyPointOutputData(k.getX(), k.getY(), k.getKind()));
        }
        return new CurveOutputData(plot.getLatex(), v.getXMin(), v.getXMax(), v.getYMin(), v.getYMax(), pieces, keys);
    }

    static SurfaceOutputData surfaceData(SurfacePlot plot) {
        SurfaceGrid grid = plot.getGrid();
        Viewport v = grid.getDomain();
        return new SurfaceOutputData(plot.getLatex(), v.getXMin(), v.getXMax(), v.getYMin(), v.getYMax(),
                grid.getZMin(), grid.getZMax(), grid.getZ());
    }
}

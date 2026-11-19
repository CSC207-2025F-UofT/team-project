package use_case.phase_portrait;

import entity.graph.Curve;
import entity.graph.Viewport;
import entity.phase.ArrowField;
import entity.phase.DifferentialEquation;
import entity.phase.Equilibrium;
import entity.phase.PhasePlot;
import entity.phase.PhaseTracePoint;

import java.util.ArrayList;
import java.util.List;

// phase plot -> plain output data -> presenter, shared by drawing + exploring
public class PhasePublisher {
    private final PhasePortraitOutputBoundary presenter;

    public PhasePublisher(PhasePortraitOutputBoundary presenter) {
        this.presenter = presenter;
    }

    public void publish(PhasePlot plot, boolean fresh) {
        presenter.preparePortraitView(data(plot), fresh);
    }

    public void publishTrace(PhasePlot plot, PhaseTracePoint point) {
        if (point == null) {
            presenter.prepareTraceView(null);
            return;
        }
        presenter.prepareTraceView(new PhaseTraceOutputData(point.getH(), point.getV(), point.getDh(), point.getDv(),
                slope(plot), point.getEquilibrium() == null ? null : equilibrium(point.getEquilibrium()),
                pieces(List.of(point.getPath()))));
    }

    static PhasePortraitOutputData data(PhasePlot plot) {
        Viewport v = plot.getViewport();
        DifferentialEquation eq = plot.getEquation();
        ArrowField a = plot.getArrows();
        List<EquilibriumOutputData> equilibria = new ArrayList<>();
        for (Equilibrium e : plot.getEquilibria()) {
            equilibria.add(equilibrium(e));
        }
        return new PhasePortraitOutputData(plot.getLatex(), slope(plot), eq.getHorizontalName(), eq.getVerticalName(),
                v.getXMin(), v.getXMax(), v.getYMin(), v.getYMax(),
                new double[][]{a.getHs(), a.getVs(), a.getDhs(), a.getDvs()}, equilibria, pieces(plot.getSolutions()));
    }

    private static boolean slope(PhasePlot plot) {
        return plot.getEquation().getKind() == DifferentialEquation.Kind.SLOPE_FIELD;
    }

    private static EquilibriumOutputData equilibrium(Equilibrium e) {
        return new EquilibriumOutputData(e.getH(), e.getV(), e.getType());
    }

    private static List<double[][]> pieces(List<Curve> curves) {
        List<double[][]> out = new ArrayList<>();
        for (Curve c : curves) {
            for (Curve.Piece p : c.getPieces()) {
                out.add(new double[][]{p.getXs(), p.getYs()});
            }
        }
        return out;
    }
}

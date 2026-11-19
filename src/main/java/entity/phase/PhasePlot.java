package entity.phase;

import entity.graph.Curve;
import entity.graph.Viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One phase portrait being explored: the equation, the window, and the solution curves pinned on it.
 * Immutable; pans, zooms and pins hand back a new one. The heavy bits are worked out on first ask.
 */
public final class PhasePlot {
    public static final int COLS = 23;
    public static final int ROWS = 15;
    static final int MAX_SOLUTIONS = 8;

    private final DifferentialEquation equation;
    private final Viewport viewport;
    private final Viewport home;
    private final List<double[]> seeds;   // points the pinned solutions go through
    private final SolutionTracer tracer = new SolutionTracer();
    private ArrowField arrows;
    private List<Equilibrium> equilibria;
    private List<Curve> solutions;

    public PhasePlot(DifferentialEquation equation, Viewport viewport) {
        this(equation, viewport, viewport, List.of());
    }

    private PhasePlot(DifferentialEquation equation, Viewport viewport, Viewport home, List<double[]> seeds) {
        this.equation = equation;
        this.viewport = viewport;
        this.home = home;
        this.seeds = Collections.unmodifiableList(new ArrayList<>(seeds));
    }

    public DifferentialEquation getEquation() {
        return equation;
    }

    public String getLatex() {
        return equation.getLatex();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Viewport getHome() {
        return home;
    }

    public PhasePlot lookingAt(Viewport next) {
        return new PhasePlot(equation, next, home, seeds);
    }

    public synchronized ArrowField getArrows() {
        if (arrows == null) {
            arrows = ArrowField.sample(equation, viewport, COLS, ROWS);
        }
        return arrows;
    }

    public synchronized List<Equilibrium> getEquilibria() {
        if (equilibria == null) {
            equilibria = Collections.unmodifiableList(new EquilibriumFinder().find(equation, viewport));
        }
        return equilibria;
    }

    // pinned solution curves, re-followed for this window so they reach the new edges
    public synchronized List<Curve> getSolutions() {
        if (solutions == null) {
            List<Curve> out = new ArrayList<>();
            for (double[] s : seeds) {
                out.add(tracer.through(equation, viewport, s[0], s[1]));
            }
            solutions = Collections.unmodifiableList(out);
        }
        return solutions;
    }

    public int solutionCount() {
        return seeds.size();
    }

    /**
     * Click: on a pinned curve takes it off, anywhere else pins the curve through there.
     * snapH / snapV = how close in world units counts as "on" it.
     */
    public PhasePlot withSolutionToggled(double h, double v, double snapH, double snapV) {
        List<Curve> curves = getSolutions();
        for (int i = 0; i < curves.size(); i++) {
            if (near(curves.get(i), h, v, snapH, snapV) || near(seeds.get(i), h, v, snapH, snapV)) {
                List<double[]> fewer = new ArrayList<>(seeds);
                fewer.remove(i);
                return new PhasePlot(equation, viewport, home, fewer);
            }
        }
        PhaseTracePoint at = trace(h, v, snapH, snapV);
        List<double[]> more = new ArrayList<>(seeds);
        more.add(new double[]{at.getH(), at.getV()});
        while (more.size() > MAX_SOLUTIONS) {
            more.remove(0);   // oldest goes
        }
        return new PhasePlot(equation, viewport, home, more);
    }

    public PhasePlot withoutSolutions() {
        return new PhasePlot(equation, viewport, home, List.of());
    }

    /** The real field at the cursor; snaps onto an equilibrium within (snapH, snapV). */
    public PhaseTracePoint trace(double h, double v, double snapH, double snapV) {
        Equilibrium hit = null;
        double best = 1;
        for (Equilibrium e : getEquilibria()) {
            double d = e.isLine() ? Math.abs(e.getV() - v) / snapV
                    : Math.hypot((e.getH() - h) / snapH, (e.getV() - v) / snapV);
            if (d <= best) {
                best = d;
                hit = e;
            }
        }
        double ph = hit == null || hit.isLine() ? h : hit.getH();
        double pv = hit == null ? v : hit.getV();
        Curve path = hit != null && !hit.isLine() ? new Curve(List.of()) : tracer.through(equation, viewport, ph, pv);
        return new PhaseTracePoint(ph, pv, equation.dh(ph, pv), equation.dv(ph, pv), hit, path);
    }

    private static boolean near(double[] p, double h, double v, double snapH, double snapV) {
        return Math.hypot((p[0] - h) / snapH, (p[1] - v) / snapV) <= 1;
    }

    private static boolean near(Curve c, double h, double v, double snapH, double snapV) {
        for (Curve.Piece piece : c.getPieces()) {
            double[] xs = piece.getXs();
            double[] ys = piece.getYs();
            for (int i = 0; i < xs.length; i++) {
                if (Math.hypot((xs[i] - h) / snapH, (ys[i] - v) / snapV) <= 1) {
                    return true;
                }
            }
        }
        return false;
    }
}

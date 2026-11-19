package entity.phase;

import entity.LatexParseException;
import entity.graph.Curve;
import entity.graph.Viewport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhasePlotTest {
    private final DifferentialEquationParser parser = new DifferentialEquationParser();
    private final Viewport box = new Viewport(-5, 5, -4, 4);

    private PhasePlot plot(String latex) throws LatexParseException {
        return new PhasePlot(parser.parse(latex), box);
    }

    @Test
    void pendulumHasACenterAndTwoSaddles() throws LatexParseException {
        List<Equilibrium> eq = plot("\\theta''=-\\sin\\theta").getEquilibria();
        assertEquals(3, eq.size());
        for (Equilibrium e : eq) {
            boolean origin = Math.abs(e.getH()) < 1e-9;
            assertEquals(origin ? Equilibrium.Type.CENTER : Equilibrium.Type.SADDLE, e.getType());
            assertTrue(origin || Math.abs(Math.abs(e.getH()) - Math.PI) < 1e-9);
        }
    }

    @Test
    void classifiesTheLinearCases() throws LatexParseException {
        assertEquals(Equilibrium.Type.STABLE_SPIRAL, only("x'=y\\\\y'=-x-0.5y"));
        assertEquals(Equilibrium.Type.UNSTABLE_SPIRAL, only("x'=y\\\\y'=-x+0.5y"));
        assertEquals(Equilibrium.Type.STABLE_NODE, only("x'=-x\\\\y'=-2y"));
        assertEquals(Equilibrium.Type.UNSTABLE_NODE, only("x'=x\\\\y'=2y"));
        assertEquals(Equilibrium.Type.SADDLE, only("x'=x\\\\y'=-y"));
    }

    private Equilibrium.Type only(String latex) throws LatexParseException {
        List<Equilibrium> eq = plot(latex).getEquilibria();
        assertEquals(1, eq.size());
        return eq.get(0).getType();
    }

    @Test
    void logisticHasAStableAndAnUnstableLine() throws LatexParseException {
        List<Equilibrium> eq = plot("y'=y(1-y)").getEquilibria();
        assertEquals(2, eq.size());
        assertEquals(0, eq.get(0).getV(), 1e-9);
        assertEquals(Equilibrium.Type.UNSTABLE_LINE, eq.get(0).getType());
        assertEquals(1, eq.get(1).getV(), 1e-9);
        assertEquals(Equilibrium.Type.STABLE_LINE, eq.get(1).getType());
        assertTrue(eq.get(1).isLine());
    }

    @Test
    void noFakeLinesWhereTheSlopeIsOnlyZeroAtOnePlace() throws LatexParseException {
        assertTrue(plot("y'=x-y").getEquilibria().isEmpty());
        assertEquals(Equilibrium.Type.LINE, plot("y'=x y").getEquilibria().get(0).getType());
    }

    @Test
    void traceReadsTheRealArrowAndFollowsTheSolution() throws LatexParseException {
        PhaseTracePoint t = plot("y'=x-y").trace(1, 3, 0.01, 0.01);
        assertEquals(1, t.getH(), 1e-12);
        assertEquals(3, t.getV(), 1e-12);
        assertEquals(-2, t.getDv(), 1e-12);
        assertNull(t.getEquilibrium());
        Curve.Piece path = t.getPath().getPieces().get(0);
        double[] xs = path.getXs();
        double[] ys = path.getYs();
        // y = x - 1 + C e^{-x} through (1, 3): C = 3e, check a far point on it
        int last = xs.length - 1;
        double exact = xs[last] - 1 + 3 * Math.E * Math.exp(-xs[last]);
        assertEquals(exact, ys[last], 1e-3 * Math.max(1, Math.abs(exact)));
        assertTrue(xs[0] < -4 || ys[0] > 4, "goes back out of the window");
    }

    @Test
    void closedOrbitComesBackToTheStart() throws LatexParseException {
        double[] xs = plot("x'=y\\\\y'=-x").trace(2, 0, 0.01, 0.01).getPath().getPieces().get(0).getXs();
        double max = 0;
        for (double x : xs) {
            max = Math.max(max, Math.abs(x));
        }
        assertEquals(2, max, 1e-3);   // circle radius 2, not a spiral out
    }

    @Test
    void traceSnapsOntoEquilibria() throws LatexParseException {
        PhaseTracePoint p = plot("x'=y\\\\y'=-x").trace(0.05, -0.04, 0.1, 0.1);
        assertEquals(0, p.getH(), 1e-12);
        assertEquals(Equilibrium.Type.CENTER, p.getEquilibrium().getType());
        assertTrue(p.getPath().isEmpty());
        PhaseTracePoint line = plot("y'=y(1-y)").trace(2, 1.05, 0.1, 0.1);
        assertEquals(2, line.getH(), 1e-12);
        assertEquals(1, line.getV(), 1e-9);
    }

    @Test
    void clickPinsASolutionAndClickingItAgainTakesItOff() throws LatexParseException {
        PhasePlot p = plot("x'=y\\\\y'=-x");
        PhasePlot pinned = p.withSolutionToggled(2, 0, 0.1, 0.1);
        assertEquals(1, pinned.solutionCount());
        assertFalse(pinned.getSolutions().get(0).isEmpty());
        // anywhere on that circle takes it off
        assertEquals(0, pinned.withSolutionToggled(0, -2, 0.1, 0.1).solutionCount());
        // panning keeps it and re-follows it
        PhasePlot moved = pinned.lookingAt(box.panned(1, 0));
        assertEquals(1, moved.solutionCount());
        assertEquals(box, moved.getHome());
        assertEquals(0, moved.withoutSolutions().solutionCount());
    }

    @Test
    void onlyKeepsTheNewestFewSolutions() throws LatexParseException {
        PhasePlot p = plot("y'=x-y");
        for (int i = 0; i < PhasePlot.MAX_SOLUTIONS + 3; i++) {
            p = p.withSolutionToggled(-4.5, -3.5 + i * 0.7, 0.01, 0.01);
        }
        assertEquals(PhasePlot.MAX_SOLUTIONS, p.solutionCount());
    }

    @Test
    void arrowsSitInsideTheWindow() throws LatexParseException {
        ArrowField a = plot("y'=x-y").getArrows();
        assertEquals(PhasePlot.COLS * PhasePlot.ROWS, a.size());
        for (double h : a.getHs()) {
            assertTrue(box.containsX(h));
        }
        assertEquals(a.getHs()[0] - a.getVs()[0], a.getDvs()[0], 1e-12);
    }
}

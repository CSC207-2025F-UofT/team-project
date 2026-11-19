package use_case.graph_function;

import data_access.InMemoryGraphSessionDataAccessObject;
import entity.LatexFunctionParser;
import entity.graph.CurvePlot;
import entity.graph.SurfacePlot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphFunctionInteractorTest {

    // remembers what came back
    static class RecordingPresenter implements GraphFunctionOutputBoundary {
        CurveOutputData curve;
        SurfaceOutputData surface;
        TraceOutputData trace;
        boolean traceCleared;
        Boolean fresh;
        String error;

        @Override
        public void prepareCurveView(CurveOutputData data, boolean fresh) {
            curve = data;
            this.fresh = fresh;
        }

        @Override
        public void prepareSurfaceView(SurfaceOutputData data, boolean fresh) {
            surface = data;
            this.fresh = fresh;
        }

        @Override
        public void prepareTraceView(TraceOutputData data) {
            trace = data;
            traceCleared = data == null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }

    private final RecordingPresenter presenter = new RecordingPresenter();
    private final InMemoryGraphSessionDataAccessObject session = new InMemoryGraphSessionDataAccessObject();
    private final GraphFunctionInteractor interactor =
            new GraphFunctionInteractor(new LatexFunctionParser(), session, presenter);

    private static int gaps(CurveOutputData data) {
        return data.getPieces().size() - 1;
    }

    @Test
    void curveComesBackAsLinePiecesInItsWindow() {
        interactor.execute(new GraphFunctionInputData("y=x^2", -2, 2));

        assertNull(presenter.error);
        assertTrue(presenter.fresh);
        CurveOutputData data = presenter.curve;
        assertEquals("y=x^2", data.getLatex());
        assertEquals(-2, data.getXMin(), 1e-9);
        assertEquals(2, data.getXMax(), 1e-9);
        assertTrue(data.getYMin() < 0 && data.getYMax() > 4);
        assertEquals(1, data.getPieces().size());
        double[][] piece = data.getPieces().get(0);
        assertEquals(piece[0][100] * piece[0][100], piece[1][100], 1e-9, "points are the real f(x)");
        assertTrue(session.current() instanceof CurvePlot);
    }

    @Test
    void yOnTheRightGraphsTheSameAsYOnTheLeft() {
        interactor.execute(new GraphFunctionInputData("y=x^2", -2, 2));
        double[] left = presenter.curve.getPieces().get(0)[1];
        interactor.execute(new GraphFunctionInputData("x^2=y", -2, 2));
        assertNull(presenter.error);
        assertArrayEquals(left, presenter.curve.getPieces().get(0)[1], 1e-12);
    }

    @Test
    void keyPointsComeAlong() {
        interactor.execute(new GraphFunctionInputData("y=x^2-1", -3, 3));
        assertTrue(presenter.curve.getKeyPoints().size() >= 3, "two roots + the low / intercept");
    }

    @Test
    void asymptoteIsClippedAndBroken() {
        interactor.execute(new GraphFunctionInputData("\\tan x"));
        assertTrue(presenter.curve.getYMax() < 1000, "window should ignore values near the asymptotes");
        assertTrue(gaps(presenter.curve) >= 6, "tan x on [-10, 10] has 6 asymptotes");
    }

    @Test
    void constantFunctionGetsANonEmptyWindow() {
        interactor.execute(new GraphFunctionInputData("y=5"));
        assertTrue(presenter.curve.getYMin() < 5 && presenter.curve.getYMax() > 5);
    }

    @Test
    void givenYRangeIsUsedExactly() {
        interactor.execute(new GraphFunctionInputData("y=x^2", -10, 10, 0, 25));
        assertEquals(0, presenter.curve.getYMin(), 1e-9);
        assertEquals(25, presenter.curve.getYMax(), 1e-9);
    }

    @Test
    void surfaceIsGraphedIn3d() {
        interactor.execute(new GraphFunctionInputData("z=x^2+y^2", -2, 2));
        assertNull(presenter.error);
        assertNull(presenter.curve);
        SurfaceOutputData data = presenter.surface;
        assertEquals(-2, data.getYMin(), 1e-9, "no y range typed: same as x");
        assertEquals(8, data.getZ()[0][0], 1e-9);
        assertTrue(data.getZMin() <= 0.1 && data.getZMax() >= 7.9);
        assertTrue(session.current() instanceof SurfacePlot);
    }

    @Test
    void surfaceUsesATypedYRange() {
        interactor.execute(new GraphFunctionInputData("f(x,y)=xy", -1, 1, 0, 3));
        assertEquals(0, presenter.surface.getYMin(), 1e-9);
        assertEquals(3, presenter.surface.getYMax(), 1e-9);
    }

    @Test
    void invalidYRangeIsReported() {
        interactor.execute(new GraphFunctionInputData("y=x", -10, 10, 5, 5));
        assertNull(presenter.curve);
        assertTrue(presenter.error.contains("y range"));
    }

    @Test
    void curveEntirelyOutsideTheYRangeIsReported() {
        interactor.execute(new GraphFunctionInputData("y=x^2+100", -10, 10, -5, 5));
        assertNull(presenter.curve);
        assertTrue(presenter.error.contains("outside the y range"));
    }

    @Test
    void parseErrorIsReported() {
        interactor.execute(new GraphFunctionInputData("y=ax+b"));
        assertNull(presenter.curve);
        assertTrue(presenter.error.contains("Unknown variable 'a'"), presenter.error);
    }

    @Test
    void emptyInputIsReported() {
        interactor.execute(new GraphFunctionInputData("  "));
        assertEquals("No expression to graph.", presenter.error);
    }

    @Test
    void undefinedEverywhereIsReported() {
        interactor.execute(new GraphFunctionInputData("\\sqrt{x}", -5, -1));
        assertTrue(presenter.error.contains("undefined"));
        interactor.execute(new GraphFunctionInputData("z=\\sqrt{-x^2-y^2-1}", -5, 5));
        assertTrue(presenter.error.contains("undefined everywhere"));
    }

    @Test
    void invalidRangeIsReported() {
        interactor.execute(new GraphFunctionInputData("x", 3, 3));
        assertTrue(presenter.error.contains("x range"));
    }
}

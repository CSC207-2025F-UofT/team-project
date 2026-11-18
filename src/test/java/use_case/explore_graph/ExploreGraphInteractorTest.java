package use_case.explore_graph;

import data_access.InMemoryGraphSessionDataAccessObject;
import entity.LatexFunctionParser;
import entity.graph.KeyPoint;
import org.junit.jupiter.api.Test;
import use_case.graph_function.CurveOutputData;
import use_case.graph_function.GraphFunctionInputData;
import use_case.graph_function.GraphFunctionInteractor;
import use_case.graph_function.GraphFunctionOutputBoundary;
import use_case.graph_function.SurfaceOutputData;
import use_case.graph_function.TraceOutputData;

import static org.junit.jupiter.api.Assertions.*;

class ExploreGraphInteractorTest {
    private CurveOutputData curve;
    private SurfaceOutputData surface;
    private TraceOutputData trace;
    private boolean fresh;

    private final GraphFunctionOutputBoundary presenter = new GraphFunctionOutputBoundary() {
        @Override
        public void prepareCurveView(CurveOutputData data, boolean isFresh) {
            curve = data;
            fresh = isFresh;
        }

        @Override
        public void prepareSurfaceView(SurfaceOutputData data, boolean isFresh) {
            surface = data;
            fresh = isFresh;
        }

        @Override
        public void prepareTraceView(TraceOutputData data) {
            trace = data;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            fail(errorMessage);
        }
    };

    private final InMemoryGraphSessionDataAccessObject session = new InMemoryGraphSessionDataAccessObject();
    private final GraphFunctionInteractor graph = new GraphFunctionInteractor(new LatexFunctionParser(), session, presenter);
    private final ExploreGraphInteractor explore = new ExploreGraphInteractor(session, presenter);

    @Test
    void nothingGraphedMeansNothingHappens() {
        explore.pan(1, 1);
        explore.zoom(0, 0, 2, 2);
        explore.reset();
        explore.trace(0, 0, 0.1);
        assertNull(curve);
        assertNull(trace);
    }

    @Test
    void panMovesTheWindowAndResamples() {
        graph.execute(new GraphFunctionInputData("y=x", -10, 10, -10, 10));
        explore.pan(5, -2);
        assertFalse(fresh, "a pan is not a new graph");
        assertEquals(-5, curve.getXMin(), 1e-9);
        assertEquals(15, curve.getXMax(), 1e-9);
        assertEquals(-12, curve.getYMin(), 1e-9);
        double[] xs = curve.getPieces().get(0)[0];
        assertTrue(xs[xs.length - 1] >= 15, "sampled over the new window");
    }

    @Test
    void zoomThenResetGoesHome() {
        graph.execute(new GraphFunctionInputData("y=x", -10, 10, -10, 10));
        explore.zoom(0, 0, 0.5, 0.5);
        assertEquals(10, curve.getXMax() - curve.getXMin(), 1e-9);
        explore.reset();
        assertEquals(-10, curve.getXMin(), 1e-9);
        assertEquals(10, curve.getXMax(), 1e-9);
    }

    @Test
    void traceReadsTheRealValueAndSnapsToKeyPoints() {
        graph.execute(new GraphFunctionInputData("y=x^2-4", -5, 5));
        explore.trace(1.25, 123, 0.01);
        assertEquals(1.25 * 1.25 - 4, trace.getY(), 1e-12);
        assertFalse(trace.isSurface());
        explore.trace(2.05, 0, 0.1);
        assertEquals(KeyPoint.Kind.ROOT, trace.getKind());
        assertEquals(2, trace.getX(), 1e-9);
        explore.endTrace();
        assertNull(trace);
    }

    @Test
    void surfaceTraceGivesZAndZoomResamplesTheGrid() {
        graph.execute(new GraphFunctionInputData("z=xy", -4, 4));
        explore.trace(2, 3, 0);
        assertEquals(6, trace.getZ(), 1e-12);
        assertTrue(trace.isSurface());
        explore.zoom(0, 0, 0.5, 0.5);
        assertEquals(-2, surface.getXMin(), 1e-9);
        assertEquals(4, surface.getZ()[0][0], 1e-9, "corner is now (-2, -2)");
    }
}

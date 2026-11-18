package interface_adapter.graph;

import entity.graph.KeyPoint;
import org.junit.jupiter.api.Test;
import use_case.graph_function.CurveOutputData;
import use_case.graph_function.KeyPointOutputData;
import use_case.graph_function.SurfaceOutputData;
import use_case.graph_function.TraceOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphPresenterTest {
    private final GraphViewModel viewModel = new GraphViewModel();
    private final GraphPresenter presenter = new GraphPresenter(viewModel);
    private final List<String> fired = new ArrayList<>();

    {
        viewModel.addPropertyChangeListener(e -> fired.add(e.getPropertyName()));
    }

    private static CurveOutputData line(double xMin, double xMax) {
        return new CurveOutputData("y=x", xMin, xMax, -5, 5,
                List.<double[][]>of(new double[][]{{xMin, xMax}, {xMin, xMax}}),
                List.of(new KeyPointOutputData(0, 0, KeyPoint.Kind.ROOT)));
    }

    @Test
    void newGraphFiresStatePanFiresView() {
        presenter.prepareCurveView(line(-5, 5), true);
        long first = viewModel.getState().getRevision();
        presenter.prepareCurveView(line(-4, 6), false);
        assertEquals(List.of(GraphViewModel.STATE_PROPERTY, GraphViewModel.VIEW_PROPERTY), fired);
        assertEquals(first, viewModel.getState().getRevision(), "a pan is the same graph");
        assertEquals(-4, viewModel.getState().getXMin(), 1e-12);
        presenter.prepareCurveView(line(-5, 5), true);
        assertEquals(first + 1, viewModel.getState().getRevision());
    }

    @Test
    void curveStateHasTicksAndCaptionedMarkers() {
        presenter.prepareCurveView(line(-5, 5), true);
        GraphState s = viewModel.getState();
        assertEquals(GraphState.Kind.CURVE, s.getKind());
        assertFalse(s.getXTicks().isEmpty());
        assertFalse(s.getYTicks().isEmpty());
        assertEquals("x-intercept", s.getMarkers().get(0).getCaption());
        assertNull(s.getZ());
    }

    @Test
    void surfaceStateHasZTicks() {
        presenter.prepareSurfaceView(new SurfaceOutputData("z=xy", -1, 1, -1, 1, -1, 1,
                new double[][]{{1, -1}, {-1, 1}}), true);
        GraphState s = viewModel.getState();
        assertEquals(GraphState.Kind.SURFACE, s.getKind());
        assertFalse(s.getZTicks().isEmpty());
        assertTrue(s.getPieces().isEmpty());
    }

    @Test
    void traceIsFormattedAndClearsOnTheNextWindow() {
        presenter.prepareCurveView(line(-5, 5), true);
        presenter.prepareTraceView(new TraceOutputData(1.5, 2.25, Double.NaN, KeyPoint.Kind.MINIMUM));
        GraphTrace t = viewModel.getState().getTrace();
        assertEquals("(1.5, 2.25)", t.getText());
        assertEquals("Minimum", t.getCaption());
        presenter.prepareTraceView(new TraceOutputData(1, 2, 3, null));
        assertEquals("(1, 2, 3)", viewModel.getState().getTrace().getText());
        presenter.prepareCurveView(line(-4, 6), false);
        assertNull(viewModel.getState().getTrace());
    }

    @Test
    void failKeepsTheOldGraphUp() {
        presenter.prepareCurveView(line(-5, 5), true);
        presenter.prepareFailView("nope");
        assertEquals("nope", viewModel.getState().getError());
        assertTrue(viewModel.getState().hasGraph());
    }
}

package interface_adapter.graph;

import entity.graph.KeyPoint;
import use_case.graph_function.CurveOutputData;
import use_case.graph_function.GraphFunctionOutputBoundary;
import use_case.graph_function.KeyPointOutputData;
import use_case.graph_function.SurfaceOutputData;
import use_case.graph_function.TraceOutputData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// use case output -> GraphState: ticks, captions, readout text; fresh graphs fire STATE, the rest VIEW
public class GraphPresenter implements GraphFunctionOutputBoundary {
    static final int X_TICKS = 8;
    static final int Y_TICKS = 6;
    static final int SURFACE_TICKS = 4;

    private final GraphViewModel viewModel;

    public GraphPresenter(GraphViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareCurveView(CurveOutputData data, boolean fresh) {
        GraphState state = next(fresh);
        state.setKind(GraphState.Kind.CURVE);
        state.setLatex(data.getLatex());
        state.setWindow(data.getXMin(), data.getXMax(), data.getYMin(), data.getYMax());
        state.setPieces(data.getPieces());
        List<GraphMarker> markers = new ArrayList<>();
        for (KeyPointOutputData k : data.getKeyPoints()) {
            markers.add(new GraphMarker(k.getX(), k.getY(), caption(k.getKind())));
        }
        state.setMarkers(markers);
        state.setZ(null);
        state.setTicks(GraphNumbers.ticks(data.getXMin(), data.getXMax(), X_TICKS),
                GraphNumbers.ticks(data.getYMin(), data.getYMax(), Y_TICKS), Collections.emptyList());
        publish(state, fresh);
    }

    @Override
    public void prepareSurfaceView(SurfaceOutputData data, boolean fresh) {
        GraphState state = next(fresh);
        state.setKind(GraphState.Kind.SURFACE);
        state.setLatex(data.getLatex());
        state.setWindow(data.getXMin(), data.getXMax(), data.getYMin(), data.getYMax());
        state.setZRange(data.getZMin(), data.getZMax());
        state.setZ(data.getZ());
        state.setPieces(Collections.emptyList());
        state.setMarkers(Collections.emptyList());
        state.setTicks(GraphNumbers.ticks(data.getXMin(), data.getXMax(), SURFACE_TICKS),
                GraphNumbers.ticks(data.getYMin(), data.getYMax(), SURFACE_TICKS),
                GraphNumbers.ticks(data.getZMin(), data.getZMax(), SURFACE_TICKS));
        publish(state, fresh);
    }

    @Override
    public void prepareTraceView(TraceOutputData data) {
        GraphState state = new GraphState(viewModel.getState());
        state.setTrace(data == null ? null
                : new GraphTrace(data.getX(), data.getY(), data.getZ(), caption(data.getKind())));
        viewModel.setState(state);
        viewModel.fireViewChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        GraphState state = new GraphState(viewModel.getState());
        state.setError(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    // null for a plain point
    static String caption(KeyPoint.Kind kind) {
        if (kind == null) {
            return null;
        }
        switch (kind) {
            case ROOT:
                return "x-intercept";
            case Y_INTERCEPT:
                return "y-intercept";
            case MINIMUM:
                return "Minimum";
            default:
                return "Maximum";
        }
    }

    private GraphState next(boolean fresh) {
        GraphState state = new GraphState(viewModel.getState());
        state.setError(null);
        state.setTrace(null);   // old readout is for the old window
        if (fresh) {
            state.setRevision(state.getRevision() + 1);
        }
        return state;
    }

    private void publish(GraphState state, boolean fresh) {
        viewModel.setState(state);
        if (fresh) {
            viewModel.firePropertyChanged();
        } else {
            viewModel.fireViewChanged();
        }
    }
}

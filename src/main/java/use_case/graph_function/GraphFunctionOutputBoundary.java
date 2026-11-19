package use_case.graph_function;

// how graphing reports back, fresh = a brand new graph, not just a pan / zoom of the old one
public interface GraphFunctionOutputBoundary {
    void prepareCurveView(CurveOutputData data, boolean fresh);

    void prepareSurfaceView(SurfaceOutputData data, boolean fresh);

    // null = cursor left, hide the readout
    void prepareTraceView(TraceOutputData data);

    void prepareFailView(String errorMessage);
}

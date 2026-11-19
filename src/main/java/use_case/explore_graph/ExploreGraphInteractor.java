package use_case.explore_graph;

import entity.graph.Plot;
import entity.graph.Viewport;
import use_case.graph_function.GraphFunctionOutputBoundary;
import use_case.graph_function.GraphSessionDataAccessInterface;
import use_case.graph_function.PlotPublisher;

import java.util.function.UnaryOperator;

// pan / zoom / reset re-sample the graph that's up, trace reads the real value under the cursor
public class ExploreGraphInteractor implements ExploreGraphInputBoundary {
    private final GraphSessionDataAccessInterface session;
    private final PlotPublisher publisher;

    public ExploreGraphInteractor(GraphSessionDataAccessInterface session, GraphFunctionOutputBoundary presenter) {
        this.session = session;
        this.publisher = new PlotPublisher(presenter);
    }

    @Override
    public void pan(double dx, double dy) {
        move(v -> v.panned(dx, dy));
    }

    @Override
    public void zoom(double cx, double cy, double fx, double fy) {
        move(v -> v.zoomed(cx, cy, fx, fy));
    }

    @Override
    public void reset() {
        Plot plot = session.current();
        if (plot != null) {
            show(plot.lookingAt(plot.getHome()));
        }
    }

    @Override
    public void trace(double x, double y, double snapX) {
        Plot plot = session.current();
        if (plot != null) {
            publisher.publishTrace(plot.trace(x, y, snapX));
        }
    }

    @Override
    public void endTrace() {
        publisher.publishTrace(null);
    }

    private void move(UnaryOperator<Viewport> change) {
        Plot plot = session.current();
        if (plot == null) {
            return;
        }
        Viewport next = change.apply(plot.getViewport());
        if (!next.equals(plot.getViewport())) {
            show(plot.lookingAt(next));
        }
    }

    private void show(Plot plot) {
        session.save(plot);
        publisher.publish(plot, false);
    }
}

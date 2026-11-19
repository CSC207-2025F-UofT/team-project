package use_case.explore_phase;

import entity.graph.Viewport;
import entity.phase.PhasePlot;
import use_case.phase_portrait.PhasePortraitOutputBoundary;
import use_case.phase_portrait.PhasePublisher;
import use_case.phase_portrait.PhaseSessionDataAccessInterface;

import java.util.function.UnaryOperator;

// pan / zoom / pin re-work the portrait that's up, trace reads the real field under the cursor
public class ExplorePhaseInteractor implements ExplorePhaseInputBoundary {
    private final PhaseSessionDataAccessInterface session;
    private final PhasePublisher publisher;

    public ExplorePhaseInteractor(PhaseSessionDataAccessInterface session, PhasePortraitOutputBoundary presenter) {
        this.session = session;
        this.publisher = new PhasePublisher(presenter);
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
        PhasePlot plot = session.current();
        if (plot != null) {
            show(plot.withoutSolutions().lookingAt(plot.getHome()));
        }
    }

    @Override
    public void trace(double h, double v, double snapH, double snapV) {
        PhasePlot plot = session.current();
        if (plot != null) {
            publisher.publishTrace(plot, plot.trace(h, v, snapH, snapV));
        }
    }

    @Override
    public void endTrace() {
        publisher.publishTrace(null, null);
    }

    @Override
    public void toggleSolution(double h, double v, double snapH, double snapV) {
        PhasePlot plot = session.current();
        if (plot != null) {
            show(plot.withSolutionToggled(h, v, snapH, snapV));
        }
    }

    private void move(UnaryOperator<Viewport> change) {
        PhasePlot plot = session.current();
        if (plot == null) {
            return;
        }
        Viewport next = change.apply(plot.getViewport());
        if (!next.equals(plot.getViewport())) {
            show(plot.lookingAt(next));
        }
    }

    private void show(PhasePlot plot) {
        session.save(plot);
        publisher.publish(plot, false);
    }
}

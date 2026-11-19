package use_case.phase_portrait;

import entity.LatexParseException;
import entity.graph.Viewport;
import entity.phase.ArrowField;
import entity.phase.DifferentialEquation;
import entity.phase.DifferentialEquationParser;
import entity.phase.PhasePlot;

// ode latex -> live phase portrait (slope field or phase plane), all plain java
public class PhasePortraitInteractor implements PhasePortraitInputBoundary {
    // no y range typed: this much of the x half-width up and down, about the card's shape
    static final double AUTO_Y_SHARE = 0.66;

    private final DifferentialEquationParser parser;
    private final PhaseSessionDataAccessInterface session;
    private final PhasePortraitOutputBoundary presenter;
    private final PhasePublisher publisher;

    public PhasePortraitInteractor(DifferentialEquationParser parser, PhaseSessionDataAccessInterface session,
                                   PhasePortraitOutputBoundary presenter) {
        this.parser = parser;
        this.session = session;
        this.presenter = presenter;
        this.publisher = new PhasePublisher(presenter);
    }

    @Override
    public void execute(PhasePortraitInputData input) {
        String latex = input.getLatex();
        if (latex == null || latex.trim().isEmpty()) {
            presenter.prepareFailView("No differential equation to draw.");
            return;
        }
        if (!(input.getXMin() < input.getXMax())) {
            presenter.prepareFailView("The x range is invalid: x min must be less than x max.");
            return;
        }
        if (!input.isYAutomatic() && !(input.getYMin() < input.getYMax())) {
            presenter.prepareFailView("The y range is invalid: y min must be less than y max.");
            return;
        }
        DifferentialEquation eq;
        try {
            eq = parser.parse(latex.trim());
        } catch (LatexParseException e) {
            presenter.prepareFailView("Cannot draw \"" + latex.trim() + "\": " + e.getMessage());
            return;
        }
        PhasePlot plot = new PhasePlot(eq, window(input));
        if (undefinedEverywhere(plot.getArrows())) {
            presenter.prepareFailView("\"" + eq.getLatex() + "\" is undefined everywhere in this window.");
            return;
        }
        session.save(plot);
        publisher.publish(plot, true);
    }

    private static Viewport window(PhasePortraitInputData input) {
        if (!input.isYAutomatic()) {
            return new Viewport(input.getXMin(), input.getXMax(), input.getYMin(), input.getYMax());
        }
        double half = (input.getXMax() - input.getXMin()) / 2 * AUTO_Y_SHARE;
        return new Viewport(input.getXMin(), input.getXMax(), -half, half);
    }

    private static boolean undefinedEverywhere(ArrowField a) {
        double[] dh = a.getDhs();
        double[] dv = a.getDvs();
        for (int i = 0; i < dh.length; i++) {
            if (Double.isFinite(dh[i]) && Double.isFinite(dv[i])) {
                return false;
            }
        }
        return true;
    }
}

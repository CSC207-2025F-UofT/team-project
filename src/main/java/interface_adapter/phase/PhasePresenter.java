package interface_adapter.phase;

import entity.phase.DifferentialEquationParser;
import entity.phase.Equilibrium;
import interface_adapter.graph.GraphNumbers;
import use_case.phase_portrait.EquilibriumOutputData;
import use_case.phase_portrait.PhasePortraitOutputBoundary;
import use_case.phase_portrait.PhasePortraitOutputData;
import use_case.phase_portrait.PhaseTraceOutputData;

import java.util.ArrayList;
import java.util.List;

// use case output -> PhaseState: ticks, axis names, equilibrium names, readout text
public class PhasePresenter implements PhasePortraitOutputBoundary {
    static final int X_TICKS = 8;
    static final int Y_TICKS = 6;

    private final PhaseViewModel viewModel;

    public PhasePresenter(PhaseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void preparePortraitView(PhasePortraitOutputData data, boolean fresh) {
        PhaseState state = new PhaseState(viewModel.getState());
        state.setError(null);
        state.setTrace(null);   // old readout is for the old window
        if (fresh) {
            state.setRevision(state.getRevision() + 1);
        }
        state.setKind(data.isSlopeField() ? PhaseState.Kind.SLOPE_FIELD : PhaseState.Kind.PHASE_PLANE);
        state.setLatex(title(data.getLatex()));
        state.setWindow(data.getXMin(), data.getXMax(), data.getYMin(), data.getYMax());
        state.setLabels(label(data.getHorizontalName()), label(data.getVerticalName()));
        state.setArrows(data.getArrows());
        List<PhaseMarker> markers = new ArrayList<>();
        for (EquilibriumOutputData e : data.getEquilibria()) {
            markers.add(new PhaseMarker(e.getH(), e.getV(), e.isLine(), role(e.getType()), name(e.getType())));
        }
        state.setMarkers(markers);
        state.setSolutions(data.getSolutions());
        state.setTicks(GraphNumbers.ticks(data.getXMin(), data.getXMax(), X_TICKS),
                GraphNumbers.ticks(data.getYMin(), data.getYMax(), Y_TICKS));
        viewModel.setState(state);
        if (fresh) {
            viewModel.firePropertyChanged();
        } else {
            viewModel.fireViewChanged();
        }
    }

    @Override
    public void prepareTraceView(PhaseTraceOutputData data) {
        PhaseState state = new PhaseState(viewModel.getState());
        state.setTrace(data == null ? null : readout(state, data));
        viewModel.setState(state);
        viewModel.fireViewChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        PhaseState state = new PhaseState(viewModel.getState());
        state.setError(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    // "(1.5, -2)", then the arrow: y' = 0.5 on a slope field, x' = 1, y' = -2 on a phase plane
    private static PhaseReadout readout(PhaseState s, PhaseTraceOutputData d) {
        String hText = GraphNumbers.format(d.getH());
        String vText = GraphNumbers.format(d.getV());
        String point = "(" + hText + ", " + vText + ")";
        String detail;
        if (d.isSlopeField()) {
            detail = "slope " + derivative(s.getVerticalLabel()) + " = " + GraphNumbers.format(d.getDv());
        } else {
            detail = derivative(s.getHorizontalLabel()) + " = " + GraphNumbers.format(d.getDh()) + ",  "
                    + derivative(s.getVerticalLabel()) + " = " + GraphNumbers.format(d.getDv());
        }
        String caption;
        if (d.getEquilibrium() != null) {
            caption = name(d.getEquilibrium().getType());
            if (d.getEquilibrium().isLine()) {
                caption += "  " + s.getVerticalLabel() + " = " + GraphNumbers.format(d.getEquilibrium().getV());
            }
        } else {
            caption = d.isSlopeField() ? "Solution through" : "Trajectory through";
        }
        return new PhaseReadout(d.getH(), d.getV(), d.getDh(), d.getDv(), caption, point, detail, hText, vText,
                d.getPath());
    }

    // a system goes on one line in the title bar: x' = y,  y' = -x
    static String title(String latex) {
        if (latex == null || !latex.contains("\\\\")) {
            return latex;
        }
        String flat = latex.replaceAll("\\\\(begin|end)\\{[a-zA-Z*]*\\}(\\{[a-z|]*\\})?", "")
                .replace("\\left\\{", "").replace("\\right.", "").replace("\\{", "").replace("&", "");
        return flat.replace("\\\\", ",\\quad ").trim();
    }

    // \theta' -> θ′, y'' stays y″
    static String label(String name) {
        return DifferentialEquationParser.display(name).replace("′′", "″");
    }

    static String derivative(String label) {
        if (label.endsWith("″")) {
            return label.substring(0, label.length() - 1) + "‴";
        }
        if (label.endsWith("′")) {
            return label.substring(0, label.length() - 1) + "″";
        }
        return label + "′";
    }

    static PhaseMarker.Role role(Equilibrium.Type type) {
        switch (type) {
            case STABLE_NODE:
            case STABLE_SPIRAL:
            case STABLE_LINE:
                return PhaseMarker.Role.ATTRACTOR;
            case UNSTABLE_NODE:
            case UNSTABLE_SPIRAL:
            case UNSTABLE_LINE:
                return PhaseMarker.Role.REPELLER;
            case SADDLE:
            case SEMI_STABLE_LINE:
                return PhaseMarker.Role.SADDLE;
            case CENTER:
                return PhaseMarker.Role.CENTER;
            default:
                return PhaseMarker.Role.NEUTRAL;
        }
    }

    static String name(Equilibrium.Type type) {
        switch (type) {
            case SADDLE:
                return "Saddle point";
            case STABLE_NODE:
                return "Stable node";
            case UNSTABLE_NODE:
                return "Unstable node";
            case STABLE_SPIRAL:
                return "Stable spiral";
            case UNSTABLE_SPIRAL:
                return "Unstable spiral";
            case CENTER:
                return "Center";
            case STABLE_LINE:
                return "Stable equilibrium";
            case UNSTABLE_LINE:
                return "Unstable equilibrium";
            case SEMI_STABLE_LINE:
                return "Semi-stable equilibrium";
            case LINE:
                return "Equilibrium solution";
            default:
                return "Equilibrium";
        }
    }
}

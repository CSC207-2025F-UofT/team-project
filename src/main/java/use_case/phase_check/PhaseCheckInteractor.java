package use_case.phase_check;

import entity.phase.DifferentialEquationParser;

// a derivative on one side of every line = an ode; whether it can be drawn is the button's problem
public class PhaseCheckInteractor implements PhaseCheckInputBoundary {
    private final DifferentialEquationParser parser;

    public PhaseCheckInteractor(DifferentialEquationParser parser) {
        this.parser = parser;
    }

    @Override
    public boolean isDifferentialEquation(String latex) {
        return latex != null && parser.looksLikeDifferentialEquation(latex);
    }
}

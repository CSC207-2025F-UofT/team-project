package interface_adapter.phase;

import use_case.phase_check.PhaseCheckInputBoundary;

// popup asks before showing Phase Portrait
public class PhaseCheckController {
    private final PhaseCheckInputBoundary interactor;

    public PhaseCheckController(PhaseCheckInputBoundary interactor) {
        this.interactor = interactor;
    }

    public boolean isDifferentialEquation(String latex) {
        return interactor.isDifferentialEquation(latex);
    }
}

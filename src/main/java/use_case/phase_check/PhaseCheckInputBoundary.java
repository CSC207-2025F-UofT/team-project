package use_case.phase_check;

// popup asks: is this an ode, i.e. offer Phase Portrait?
public interface PhaseCheckInputBoundary {
    boolean isDifferentialEquation(String latex);
}

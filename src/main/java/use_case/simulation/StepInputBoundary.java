package use_case.simulation;

public interface StepInputBoundary {
    /** Advances the simulation by this many seconds of real time. */
    void step(double seconds);
}

package interface_adapter.simulation;

import use_case.simulation.PlayInputBoundary;
import use_case.simulation.StepInputBoundary;
import use_case.simulation.StopInputBoundary;

public final class SimulationController {
    private final PlayInputBoundary play;
    private final StepInputBoundary step;
    private final StopInputBoundary stop;

    public SimulationController(PlayInputBoundary play, StepInputBoundary step, StopInputBoundary stop) {
        this.play = play;
        this.step = step;
        this.stop = stop;
    }

    public void play() {
        play.play();
    }

    public void step(double seconds) {
        step.step(seconds);
    }

    public void stop() {
        stop.stop();
    }
}

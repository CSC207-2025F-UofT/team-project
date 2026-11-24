package use_case.simulation;

public interface SimulationOutputBoundary {
    void preparePlayingView();

    void prepareStepView(SimulationOutputData output);

    void prepareStoppedView(SimulationOutputData output);
}

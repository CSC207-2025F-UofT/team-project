package use_case.simulation;

import entity.PhysicsEngine;
import entity.Scene;

/** Moves every ball forward by one clock tick, scaled by the scene's sim speed. */
public final class StepInteractor implements StepInputBoundary {
    private final SimulationDataAccessInterface dataAccess;
    private final SimulationOutputBoundary presenter;
    private final PhysicsEngine engine;

    public StepInteractor(SimulationDataAccessInterface dataAccess, SimulationOutputBoundary presenter,
                          PhysicsEngine engine) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.engine = engine;
    }

    @Override
    public void step(double seconds) {
        Scene scene = dataAccess.getCurrentScene();
        engine.step(scene, seconds * scene.getSimSpeed());
        presenter.prepareStepView(SimulationOutputData.from(scene));
    }
}

package use_case.simulation;

import entity.Scene;
import entity.SceneSnapshot;

/**
 * Stops the simulation and puts the balls back where they were when Play was first pressed.
 * Balls dropped after that are removed, since they were not there at the start.
 */
public final class StopInteractor implements StopInputBoundary {
    private final SimulationDataAccessInterface dataAccess;
    private final SimulationOutputBoundary presenter;

    public StopInteractor(SimulationDataAccessInterface dataAccess, SimulationOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void stop() {
        Scene scene = dataAccess.getCurrentScene();
        SceneSnapshot snapshot = dataAccess.getStartSnapshot();
        if (snapshot != null) {
            while (scene.getBalls().size() > snapshot.getBallCount()) {
                scene.removeBall(scene.getBalls().get(scene.getBalls().size() - 1));
            }
            if (scene.getBalls().size() == snapshot.getBallCount()) {
                snapshot.restore(scene);
            }
            dataAccess.setStartSnapshot(null);
        }
        presenter.prepareStoppedView(SimulationOutputData.from(scene));
    }
}

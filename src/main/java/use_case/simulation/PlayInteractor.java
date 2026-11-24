package use_case.simulation;

import entity.SceneSnapshot;

/** Starts or resumes the simulation. The first Play after a stop remembers where the balls started. */
public final class PlayInteractor implements PlayInputBoundary {
    private final SimulationDataAccessInterface dataAccess;
    private final SimulationOutputBoundary presenter;

    public PlayInteractor(SimulationDataAccessInterface dataAccess, SimulationOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void play() {
        if (dataAccess.getStartSnapshot() == null) {
            dataAccess.setStartSnapshot(SceneSnapshot.capture(dataAccess.getCurrentScene()));
        }
        presenter.preparePlayingView();
    }
}

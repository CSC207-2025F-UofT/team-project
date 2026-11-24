package use_case.simulation;

import entity.Scene;
import use_case.scene.SceneViewData;
import use_case.scene.SceneViewData.BallData;

import java.util.List;

/** Where every ball is now. The terrain does not change while the simulation runs. */
public final class SimulationOutputData {
    private final List<BallData> balls;

    public SimulationOutputData(List<BallData> balls) {
        this.balls = List.copyOf(balls);
    }

    static SimulationOutputData from(Scene scene) {
        return new SimulationOutputData(SceneViewData.from(scene, null).getBalls());
    }

    public List<BallData> getBalls() {
        return balls;
    }
}

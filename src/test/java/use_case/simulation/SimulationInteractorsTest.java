package use_case.simulation;

import entity.Ball;
import entity.ObjectPreset;
import entity.PhysicsEngine;
import entity.Scene;
import entity.SceneSnapshot;
import entity.TerrainBuilder;
import entity.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class SimulationInteractorsTest {
    private static final ObjectPreset PRESET = new ObjectPreset("Marble", 0.5, 1, 0.2, 0.3, 0x3366cc);

    private final FakeDataAccess data = new FakeDataAccess();
    private final RecordingPresenter presenter = new RecordingPresenter();
    private final RecordingEngine engine = new RecordingEngine();
    private Ball ball;

    @BeforeEach
    void setUp() {
        data.scene = new Scene(new TerrainBuilder("Flat").gridSize(10, 10).build());
        ball = new Ball(new Vector3D(5, 5, 0.5), Vector3D.ZERO, PRESET);
        data.scene.addBall(ball);
    }

    @Test
    void playRemembersTheStartOnlyOnce() {
        PlayInteractor play = new PlayInteractor(data, presenter);
        play.play();
        SceneSnapshot first = data.snapshot;
        assertNotNull(first);
        assertEquals(1, presenter.playingViews);

        play.play();
        assertSame(first, data.snapshot, "resuming after pause keeps the original start");
        assertEquals(2, presenter.playingViews);
    }

    @Test
    void stepRunsTheEngineScaledBySimSpeed() {
        data.scene.setSimSpeed(2);
        new StepInteractor(data, presenter, engine).step(0.25);
        assertSame(data.scene, engine.scene);
        assertEquals(0.5, engine.dt, 1e-12);
        assertEquals(1, presenter.lastStep.getBalls().size());
        assertEquals(6, presenter.lastStep.getBalls().get(0).getX(), 1e-12);
    }

    @Test
    void stopPutsBallsBackAndForgetsTheStart() {
        new PlayInteractor(data, presenter).play();
        ball.setPosition(new Vector3D(9, 9, 0.5));
        data.scene.addBall(new Ball(new Vector3D(1, 1, 0.5), Vector3D.ZERO, PRESET));

        new StopInteractor(data, presenter).stop();

        assertEquals(1, data.scene.getBalls().size(), "balls dropped after Play are removed");
        assertEquals(5, ball.getPosition().getX(), 1e-12);
        assertNull(data.snapshot);
        assertEquals(5, presenter.lastStop.getBalls().get(0).getX(), 1e-12);
    }

    @Test
    void stopWithoutPlayLeavesBallsWhereTheyAre() {
        ball.setPosition(new Vector3D(7, 7, 0.5));
        new StopInteractor(data, presenter).stop();
        assertEquals(7, ball.getPosition().getX(), 1e-12);
        assertEquals(1, presenter.lastStop.getBalls().size());
    }

    private static final class FakeDataAccess implements SimulationDataAccessInterface {
        private Scene scene;
        private SceneSnapshot snapshot;

        @Override
        public Scene getCurrentScene() {
            return scene;
        }

        @Override
        public SceneSnapshot getStartSnapshot() {
            return snapshot;
        }

        @Override
        public void setStartSnapshot(SceneSnapshot snapshot) {
            this.snapshot = snapshot;
        }
    }

    private static final class RecordingPresenter implements SimulationOutputBoundary {
        private int playingViews;
        private SimulationOutputData lastStep;
        private SimulationOutputData lastStop;

        @Override
        public void preparePlayingView() {
            playingViews++;
        }

        @Override
        public void prepareStepView(SimulationOutputData output) {
            lastStep = output;
        }

        @Override
        public void prepareStoppedView(SimulationOutputData output) {
            lastStop = output;
        }
    }

    /** Moves every ball 1 m along x so the test can see the step reached the output. */
    private static final class RecordingEngine implements PhysicsEngine {
        private Scene scene;
        private double dt;

        @Override
        public void step(Scene scene, double dt) {
            this.scene = scene;
            this.dt = dt;
            for (Ball ball : scene.getBalls()) {
                ball.setPosition(ball.getPosition().add(new Vector3D(1, 0, 0)));
            }
        }
    }
}

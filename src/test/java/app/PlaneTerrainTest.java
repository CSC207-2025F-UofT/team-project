package app;

import data_access.FileSceneDataAccess;
import data_access.InMemorySceneDataAccess;
import data_access.SceneTerrainMapper;
import entity.Scene;
import entity.SimplePhysicsEngine;
import entity.Vector3D;
import interface_adapter.load_scene.LoadSceneController;
import interface_adapter.save_scene.SaveSceneController;
import interface_adapter.simulation.CameraPose;
import interface_adapter.simulation.ScenePresenter;
import interface_adapter.simulation.SceneView;
import interface_adapter.simulation.SimulationController;
import interface_adapter.simulation.SimulationState;
import interface_adapter.simulation.SimulationViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.load_scene.LoadSceneInteractor;
import use_case.save_scene.SaveSceneInteractor;
import use_case.scene.SceneViewData;
import use_case.simulation.PlayInteractor;
import use_case.simulation.StepInteractor;
import use_case.simulation.StopInteractor;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaneTerrainTest {
    @TempDir
    Path directory;
    private Scene initialScene;
    private FileSceneDataAccess files;
    private ScenePresenter presenter;
    private final TestView view = new TestView();

    @BeforeEach
    void openFlatPreview() {
        SceneTerrainMapper mapper = new SceneTerrainMapper();
        initialScene = Main.flatPreviewScene();
        InMemorySceneDataAccess scenes = new InMemorySceneDataAccess(initialScene);
        files = new FileSceneDataAccess(scenes, mapper);
        SceneViewData data = SceneViewData.from(initialScene, mapper.toHeightGrid(initialScene.getTerrain()));
        presenter = new ScenePresenter(new SimulationViewModel(SimulationState.from(data, "")));
        SimulationController simulation = new SimulationController(new PlayInteractor(scenes, presenter),
                new StepInteractor(scenes, presenter, new SimplePhysicsEngine()),
                new StopInteractor(scenes, presenter));
        presenter.start(view, new SaveSceneController(new SaveSceneInteractor(files, presenter)),
                new LoadSceneController(new LoadSceneInteractor(files, presenter)), simulation);
    }

    @Test
    void playPauseAndStopDriveTheClockAndBalls() {
        CameraPose camera = view.camera;
        // Give the ball a push so it visibly moves on the flat plane.
        initialScene.getBalls().get(0).setVelocity(new Vector3D(3, 0, 0));

        presenter.clockTicked(0.1);
        assertEquals(15.0, view.state.getBalls().get(0).x, "ticks before Play are ignored");

        presenter.playRequested();
        assertTrue(view.clockRunning);
        assertEquals("Playing", view.state.getStatusText());
        presenter.clockTicked(0.5);
        assertTrue(view.state.getBalls().get(0).x > 15.0);
        assertSame(camera, view.camera, "moving balls does not re-centre the camera");

        presenter.pauseRequested();
        assertFalse(view.clockRunning);
        assertEquals("Paused", view.state.getStatusText());
        double pausedX = view.state.getBalls().get(0).x;
        presenter.clockTicked(0.5);
        assertEquals(pausedX, view.state.getBalls().get(0).x);

        presenter.playRequested();
        presenter.clockTicked(0.5);
        presenter.stopRequested();
        assertFalse(view.clockRunning);
        assertEquals("Stopped", view.state.getStatusText());
        assertPlaneWithBall(view.state);
    }

    @Test
    void loadingStopsTheClock() throws Exception {
        view.savePath = directory.resolve("plane.json");
        presenter.saveRequested();
        presenter.playRequested();
        view.loadPath = view.savePath;
        presenter.loadRequested();
        assertFalse(view.clockRunning);
        assertPlaneWithBall(view.state);
    }

    @Test
    void swingPreviewDrawsThePlaneAndItsBall() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JPanel screen = Main.createPreview(Main.flatPreviewScene());
            BorderLayout layout = (BorderLayout) screen.getLayout();
            JPanel canvas = (JPanel) layout.getLayoutComponent(BorderLayout.CENTER);
            int width = 360;
            int height = 260;
            canvas.setSize(width, height);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            try {
                canvas.paint(graphics);
            } finally {
                graphics.dispose();
            }

            int sky = image.getRGB(0, 0);
            int leftGround = image.getRGB(width / 3, height / 2);
            int rightGround = image.getRGB(2 * width / 3, height / 2);
            int ball = image.getRGB(width / 2, height / 2 - height / 32);
            assertEquals(leftGround, rightGround, "Both sides of the plane have the same shading");
            assertNotEquals(sky, leftGround, "The plane is visible");
            assertNotEquals(sky, ball);
            assertNotEquals(leftGround, ball, "The centered ball is visible against the plane");
        });
    }

    @Test
    void flatPreviewAndBallRoundTripThroughThePresenterAndJsonStore() {
        assertEquals("Flat terrain", view.state.getStatusText());
        assertPlaneWithBall(view.state);
        view.savePath = directory.resolve("plane");
        presenter.saveRequested();
        Path file = directory.resolve("plane.json");
        assertTrue(Files.isRegularFile(file));
        assertEquals("Saved plane.json", view.state.getStatusText());

        presenter.moveCamera(0, 1, 0);
        assertNotEquals(0.0, view.camera.x);
        view.loadPath = file;
        presenter.loadRequested();

        assertNotSame(initialScene, files.getCurrentScene());
        assertEquals("Loaded plane.json", view.state.getStatusText());
        assertPlaneWithBall(view.state);
        assertEquals(0.0, view.camera.x);
        assertEquals(0.0, view.camera.y);
    }

    @Test
    void cancellingAndFailedLoadsPreserveThePlane() {
        presenter.moveCamera(0, 0, 1);
        CameraPose camera = view.camera;
        SimulationState original = view.state;
        presenter.saveRequested();
        presenter.loadRequested();
        assertSame(original, view.state);

        view.loadPath = directory.resolve("missing.json");
        presenter.loadRequested();
        assertSame(initialScene, files.getCurrentScene());
        assertSame(camera, view.camera);
        assertEquals("The scene file could not be found.", view.state.getStatusText());
        assertPlaneWithBall(view.state);
    }

    @Test
    void presenterAsksTheViewBeforeReplacingAFile() throws Exception {
        view.savePath = directory.resolve("plane.json");
        Files.writeString(view.savePath, "original contents");
        presenter.saveRequested();
        assertEquals("plane.json", view.confirmedName);
        assertEquals("original contents", Files.readString(view.savePath));

        view.allowOverwrite = true;
        presenter.saveRequested();
        assertEquals("Saved plane.json", view.state.getStatusText());
        view.loadPath = view.savePath;
        presenter.loadRequested();
        assertEquals("Loaded plane.json", view.state.getStatusText());
        assertPlaneWithBall(view.state);
    }

    private static void assertPlaneWithBall(SimulationState state) {
        assertEquals(16, state.getRows());
        assertEquals(16, state.getCols());
        assertEquals(2.0, state.getCellSize());
        for (int row = 0; row < state.getRows(); row++) {
            for (int col = 0; col < state.getCols(); col++) {
                assertEquals(0.0, state.getHeight(row, col));
            }
        }
        assertEquals(1, state.getBalls().size());
        SimulationState.Ball ball = state.getBalls().get(0);
        assertEquals(15.0, ball.x);
        assertEquals(15.0, ball.y);
        assertEquals(0.0, ball.z - ball.radius);
    }

    private static final class TestView implements SceneView {
        private Path savePath;
        private Path loadPath;
        private String confirmedName;
        private boolean allowOverwrite;
        private SimulationState state;
        private CameraPose camera;
        private boolean clockRunning;

        @Override
        public void startClock() {
            clockRunning = true;
        }

        @Override
        public void stopClock() {
            clockRunning = false;
        }

        @Override
        public Path chooseSaveFile() {
            return savePath;
        }

        @Override
        public Path chooseLoadFile() {
            return loadPath;
        }

        @Override
        public boolean confirmOverwrite(String fileName) {
            confirmedName = fileName;
            return allowOverwrite;
        }

        @Override
        public void display(SimulationState state, CameraPose camera) {
            this.state = state;
            this.camera = camera;
        }
    }
}

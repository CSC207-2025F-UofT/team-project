package use_case.scene;

import entity.Ball;
import entity.ObjectPreset;
import entity.Scene;
import entity.Terrain;
import entity.Vector3D;
import org.junit.jupiter.api.Test;
import use_case.load_scene.LoadSceneDataAccessInterface;
import use_case.load_scene.LoadSceneInputData;
import use_case.load_scene.LoadSceneInteractor;
import use_case.load_scene.LoadSceneOutputBoundary;
import use_case.load_scene.LoadSceneOutputData;
import use_case.save_scene.SaveSceneDataAccessInterface;
import use_case.save_scene.SaveSceneInputData;
import use_case.save_scene.SaveSceneInteractor;
import use_case.save_scene.SaveSceneOutputBoundary;
import use_case.save_scene.SaveSceneOutputData;
import use_case.scene.SceneViewData.HeightGrid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class SceneInteractorsTest {
    private static final String FILE = "scenes/scene.json";
    private static final String NAME = "scene.json";
    private final FakeSceneDataAccess data = new FakeSceneDataAccess();
    private final RecordingPresenter presenter = new RecordingPresenter();

    @Test
    void savesTheCurrentSceneToTheSelectedPath() {
        new SaveSceneInteractor(data, presenter).execute(new SaveSceneInputData(FILE, NAME, false));

        assertSame(data.current, data.saved);
        assertEquals(FILE, data.savedPath);
        assertEquals("scene.json", presenter.saved.getFileName());
        assertNull(presenter.error);
    }

    @Test
    void reportsAnExistingFileAndRetriesOnlyAfterConfirmation() {
        data.fileExists = true;
        SaveSceneInteractor save = new SaveSceneInteractor(data, presenter);
        save.execute(new SaveSceneInputData(FILE, NAME, false));

        assertEquals(FILE, presenter.overwrite.getFilePath());
        assertNull(presenter.saved);
        assertNull(data.saved);

        save.execute(new SaveSceneInputData(FILE, NAME, true));
        assertEquals(NAME, presenter.saved.getFileName());
        assertSame(data.current, data.saved);
    }

    @Test
    void refusesToSaveWithoutTerrain() {
        data.current = new Scene();
        new SaveSceneInteractor(data, presenter).execute(new SaveSceneInputData(FILE, NAME, false));

        assertNull(data.savedPath);
        assertNull(presenter.saved);
        assertEquals("Add terrain before saving the scene.", presenter.error);
    }

    @Test
    void reportsWriteErrors() {
        data.error = "Write failed";
        new SaveSceneInteractor(data, presenter).execute(new SaveSceneInputData(FILE, NAME, false));

        assertNull(presenter.saved);
        assertEquals(data.error, presenter.error);
    }

    @Test
    void replacesTheSceneAndPresentsTheLoadedHeightGrid() {
        new LoadSceneInteractor(data, presenter).execute(new LoadSceneInputData(FILE, NAME));

        assertSame(data.loaded, data.current);
        assertEquals(FILE, data.loadedPath);
        assertSame(data.grid, presenter.loaded.getScene().getTerrain());
        assertNull(presenter.error);
    }

    @Test
    void invalidBallPresetsCannotBeSavedOrReplaceTheCurrentScene() {
        Ball ball = new Ball(Vector3D.ZERO, Vector3D.ZERO, new ObjectPreset("Marble", 1, 1, 2, 0, 0xff));
        Scene original = data.current;
        original.addBall(ball);
        new SaveSceneInteractor(data, presenter).execute(new SaveSceneInputData(FILE, NAME, false));
        assertNull(data.savedPath);
        assertEquals("A ball in the scene has an invalid preset.", presenter.error);

        data.loaded.addBall(ball);
        presenter.error = null;
        new LoadSceneInteractor(data, presenter).execute(new LoadSceneInputData(FILE, NAME));
        assertSame(original, data.current);
        assertNull(presenter.loaded);
        assertEquals("A ball in the scene has an invalid preset.", presenter.error);
    }

    @Test
    void preservesTheCurrentSceneOnReadErrors() {
        Scene original = data.current;
        data.error = "Read failed";
        new LoadSceneInteractor(data, presenter).execute(new LoadSceneInputData(FILE, NAME));

        assertSame(original, data.current);
        assertNull(presenter.loaded);
        assertEquals(data.error, presenter.error);
    }

    @Test
    void preservesTheCurrentSceneWhenLoadedTerrainCannotBePresented() {
        Scene original = data.current;
        data.grid = null;
        new LoadSceneInteractor(data, presenter).execute(new LoadSceneInputData(FILE, NAME));

        assertSame(original, data.current);
        assertNull(presenter.loaded);
        assertEquals("Missing height grid", presenter.error);
    }

    private static final class FakeSceneDataAccess implements SaveSceneDataAccessInterface, LoadSceneDataAccessInterface {
        private Scene current = new Scene(new Terrain("Current terrain", 1, new double[2][2]));
        private final Scene loaded = new Scene(new Terrain("Test terrain", 1, new double[2][2]));
        private HeightGrid grid = new HeightGrid("Test terrain", 1, new double[2][2]);
        private Scene saved;
        private String savedPath;
        private String loadedPath;
        private boolean fileExists;
        private String error;

        @Override
        public Scene getCurrentScene() {
            return current;
        }

        @Override
        public boolean save(String path, Scene scene, boolean overwrite) throws SceneFileException {
            failIfConfigured();
            if (fileExists && !overwrite) {
                return false;
            }
            saved = scene;
            savedPath = path;
            return true;
        }

        @Override
        public Scene load(String path) throws SceneFileException {
            failIfConfigured();
            loadedPath = path;
            return loaded;
        }

        @Override
        public HeightGrid getHeightGrid(Terrain terrain) throws SceneFileException {
            if (grid == null) {
                throw new SceneFileException("Missing height grid");
            }
            return grid;
        }

        @Override
        public void replaceCurrentScene(Scene scene) {
            current = scene;
        }

        private void failIfConfigured() throws SceneFileException {
            if (error != null) {
                throw new SceneFileException(error);
            }
        }
    }

    private static final class RecordingPresenter implements SaveSceneOutputBoundary, LoadSceneOutputBoundary {
        private SaveSceneOutputData saved;
        private SaveSceneOutputData overwrite;
        private LoadSceneOutputData loaded;
        private String error;

        @Override
        public void prepareSuccessView(SaveSceneOutputData output) {
            saved = output;
        }

        @Override
        public void prepareOverwriteView(SaveSceneOutputData output) {
            overwrite = output;
        }

        @Override
        public void prepareSuccessView(LoadSceneOutputData output) {
            loaded = output;
        }

        @Override
        public void prepareFailView(String error) {
            this.error = error;
        }
    }
}

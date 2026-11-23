package data_access;

import entity.Ball;
import entity.ObjectPreset;
import entity.Scene;
import entity.Terrain;
import entity.Vector3D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.scene.SceneFileException;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileSceneDataAccessTest {
    @TempDir
    Path directory;

    @Test
    void roundTripsTerrainBallsPresetsAndSettingsThroughAFreshAdapter() throws Exception {
        FileSceneDataAccess writer = adapter();
        Scene original = writer.getCurrentScene();
        original.setGravity(4.5);
        original.setSimSpeed(1.25);
        original.addBall(new Ball(new Vector3D(1, 2, 3), new Vector3D(-4, 5, -6),
                new ObjectPreset("Marble", 0.25, 0.125, 0.85, 0.1, 0xa92111)));
        original.addBall(new Ball(new Vector3D(4, 5, 6), Vector3D.ZERO,
                new ObjectPreset("Beach ball", 0.45, 0.2, 0.55, 0.3, 0xfff1cd24)));

        Path file = directory.resolve("scene.json");
        writer.save(file.toString(), original, false);
        FileSceneDataAccess reader = new FileSceneDataAccess(
                new InMemorySceneDataAccess(new Scene()), new SceneTerrainMapper());
        Scene restored = reader.load(file.toString());
        Terrain terrain = restored.getTerrain();

        assertEquals("Côte and valley", terrain.getName());
        assertEquals(2.5, terrain.getCellSize());
        assertEquals(2, terrain.getRows());
        assertEquals(3, terrain.getCols());
        assertArrayEquals(original.getTerrain().getHeights(), terrain.getHeights());
        assertEquals(4.5, restored.getGravity());
        assertEquals(1.25, restored.getSimSpeed());
        assertEquals(2, restored.getBalls().size());

        for (int index = 0; index < original.getBalls().size(); index++) {
            Ball ball = original.getBalls().get(index);
            Ball copy = restored.getBalls().get(index);
            assertVector(ball.getPosition(), copy.getPosition());
            assertVector(ball.getVelocity(), copy.getVelocity());
            assertEquals(ball.getPreset().getName(), copy.getPreset().getName());
            assertEquals(ball.getPreset().getRadius(), copy.getPreset().getRadius());
            assertEquals(ball.getPreset().getMass(), copy.getPreset().getMass());
            assertEquals(ball.getPreset().getBounciness(), copy.getPreset().getBounciness());
            assertEquals(ball.getPreset().getFriction(), copy.getPreset().getFriction());
            assertEquals(ball.getPreset().getColour(), copy.getPreset().getColour());
        }
    }

    @Test
    void rejectsMalformedMissingAndUnsupportedFiles() throws Exception {
        FileSceneDataAccess files = adapter();
        Path file = directory.resolve("invalid.json");

        assertThrows(SceneFileException.class, () -> files.load(file.toString()));
        Files.writeString(file, "{");
        assertThrows(SceneFileException.class, () -> files.load(file.toString()));
        Files.writeString(file, "{\"version\":2}");
        assertThrows(SceneFileException.class, () -> files.load(file.toString()));

        files.save(file.toString(), files.getCurrentScene(), true);
        String valid = Files.readString(file);
        Files.writeString(file, valid + "unexpected");
        assertThrows(SceneFileException.class, () -> files.load(file.toString()));
        Files.writeString(file, valid.replace("8.125", "\"8.125\""));
        assertThrows(SceneFileException.class, () -> files.load(file.toString()));
    }

    @Test
    void replacesAnExistingFileOnlyWhenOverwriteIsRequested() throws Exception {
        FileSceneDataAccess files = adapter();
        Scene scene = files.getCurrentScene();
        Path file = directory.resolve("existing.json");
        Files.writeString(file, "original");

        assertFalse(files.save(file.toString(), scene, false));
        assertEquals("original", Files.readString(file));

        assertTrue(files.save(file.toString(), scene, true));
        assertEquals(scene.getGravity(), files.load(file.toString()).getGravity());
    }

    @Test
    void invalidSaveDoesNotTruncateAnExistingScene() throws Exception {
        FileSceneDataAccess files = adapter();
        Path file = directory.resolve("existing.json");
        Scene scene = files.getCurrentScene();
        files.save(file.toString(), scene, true);
        String saved = Files.readString(file);

        scene.setSimSpeed(Double.NaN);
        assertThrows(SceneFileException.class, () -> files.save(file.toString(), scene, true));
        assertEquals(saved, Files.readString(file));
    }

    private static FileSceneDataAccess adapter() {
        Terrain terrain = new Terrain("Côte and valley", 2.5, new double[][] {{0, 2, 3}, {-4.5, 7, 8.125}});
        return new FileSceneDataAccess(new InMemorySceneDataAccess(new Scene(terrain)), new SceneTerrainMapper());
    }

    private static void assertVector(Vector3D expected, Vector3D actual) {
        assertEquals(expected.getX(), actual.getX());
        assertEquals(expected.getY(), actual.getY());
        assertEquals(expected.getZ(), actual.getZ());
    }
}

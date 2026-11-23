package data_access;

import entity.Ball;
import entity.ObjectPreset;
import entity.Scene;
import entity.Terrain;
import entity.Vector3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import use_case.load_scene.LoadSceneDataAccessInterface;
import use_case.save_scene.SaveSceneDataAccessInterface;
import use_case.scene.SceneFileException;
import use_case.scene.SceneViewData.HeightGrid;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class FileSceneDataAccess implements SaveSceneDataAccessInterface, LoadSceneDataAccessInterface {
    private static final int FORMAT_VERSION = 1;

    private final InMemorySceneDataAccess scenes;
    private final TerrainMapper terrainMapper;

    public FileSceneDataAccess(InMemorySceneDataAccess scenes, TerrainMapper terrainMapper) {
        this.scenes = scenes;
        this.terrainMapper = terrainMapper;
    }

    /** Maps terrain samples between the team entity and the scene file format. */
    public interface TerrainMapper {
        HeightGrid toHeightGrid(Terrain terrain);

        Terrain toTerrain(HeightGrid grid);
    }

    @Override
    public HeightGrid getHeightGrid(Terrain terrain) throws SceneFileException {
        HeightGrid grid = terrainMapper.toHeightGrid(terrain);
        if (grid == null) {
            throw new SceneFileException("No height data is available for this terrain.");
        }
        return grid;
    }

    @Override
    public Scene getCurrentScene() {
        return scenes.getCurrentScene();
    }

    @Override
    public void replaceCurrentScene(Scene scene) {
        scenes.replaceCurrentScene(scene);
    }

    @Override
    public boolean save(String filePath, Scene scene, boolean overwrite) throws SceneFileException {
        Path path = selectedPath(filePath);
        try {
            String contents = encodeScene(scene).toString(2) + System.lineSeparator();
            return writeFile(path, contents, overwrite);
        } catch (IOException | SecurityException e) {
            throw new SceneFileException("Could not write the scene file.", e);
        } catch (JSONException | IllegalArgumentException | NullPointerException e) {
            throw new SceneFileException("The current scene contains invalid data.", e);
        }
    }

    @Override
    public Scene load(String filePath) throws SceneFileException {
        Path path = selectedPath(filePath);
        try {
            JSONTokener input = new JSONTokener(Files.readString(path));
            JSONObject document = new JSONObject(input);
            if (input.nextClean() != 0) {
                throw new JSONException("Unexpected text after the scene");
            }
            if (readInt(document, "version") != FORMAT_VERSION) {
                throw new SceneFileException("This scene file version is not supported.");
            }
            return decodeScene(document);
        } catch (NoSuchFileException e) {
            throw new SceneFileException("The scene file could not be found.", e);
        } catch (IOException | SecurityException e) {
            throw new SceneFileException("Could not read the scene file.", e);
        } catch (JSONException | IllegalArgumentException | ArithmeticException e) {
            throw new SceneFileException("The file does not contain a valid scene.", e);
        }
    }

    private JSONObject encodeScene(Scene scene) throws SceneFileException {
        JSONArray balls = new JSONArray();
        for (Ball ball : scene.getBalls()) {
            ObjectPreset preset = ball.getPreset();
            JSONObject presetData = new JSONObject()
                    .put("name", preset.getName())
                    .put("radius", preset.getRadius())
                    .put("mass", preset.getMass())
                    .put("bounciness", preset.getBounciness())
                    .put("friction", preset.getFriction())
                    .put("colour", preset.getColour());
            balls.put(new JSONObject()
                    .put("position", encodeVector(ball.getPosition()))
                    .put("velocity", encodeVector(ball.getVelocity()))
                    .put("preset", presetData));
        }
        HeightGrid terrain = getHeightGrid(scene.getTerrain());
        JSONArray heights = new JSONArray();
        for (int row = 0; row < terrain.getRows(); row++) {
            JSONArray samples = new JSONArray();
            for (int col = 0; col < terrain.getCols(); col++) {
                samples.put(terrain.getHeight(row, col));
            }
            heights.put(samples);
        }
        return new JSONObject()
                .put("version", FORMAT_VERSION)
                .put("terrain", new JSONObject()
                        .put("name", terrain.getName())
                        .put("cellSize", terrain.getCellSize())
                        .put("heights", heights))
                .put("balls", balls)
                .put("gravity", scene.getGravity())
                .put("simSpeed", scene.getSimSpeed());
    }

    private Scene decodeScene(JSONObject document) {
        JSONObject terrainData = document.getJSONObject("terrain");
        JSONArray rows = terrainData.getJSONArray("heights");
        double[][] heights = new double[rows.length()][];
        for (int row = 0; row < heights.length; row++) {
            JSONArray samples = rows.getJSONArray(row);
            heights[row] = new double[samples.length()];
            for (int col = 0; col < samples.length(); col++) {
                heights[row][col] = readNumber(samples.get(col), "height").doubleValue();
            }
        }
        HeightGrid grid = new HeightGrid(terrainData.getString("name"),
                readDouble(terrainData, "cellSize"), heights);
        Scene scene = new Scene(terrainMapper.toTerrain(grid));
        scene.setGravity(readDouble(document, "gravity"));
        scene.setSimSpeed(readDouble(document, "simSpeed"));
        JSONArray balls = document.getJSONArray("balls");
        for (int index = 0; index < balls.length(); index++) {
            JSONObject ball = balls.getJSONObject(index);
            JSONObject presetData = ball.getJSONObject("preset");
            ObjectPreset preset = new ObjectPreset(presetData.getString("name"),
                    readDouble(presetData, "radius"), readDouble(presetData, "mass"),
                    readDouble(presetData, "bounciness"), readDouble(presetData, "friction"),
                    readInt(presetData, "colour"));
            scene.addBall(new Ball(decodeVector(ball.getJSONObject("position")),
                    decodeVector(ball.getJSONObject("velocity")), preset));
        }
        return scene;
    }

    private static JSONObject encodeVector(Vector3D vector) {
        return new JSONObject().put("x", vector.getX()).put("y", vector.getY()).put("z", vector.getZ());
    }

    private static Vector3D decodeVector(JSONObject vector) {
        return new Vector3D(readDouble(vector, "x"), readDouble(vector, "y"), readDouble(vector, "z"));
    }

    private static double readDouble(JSONObject object, String key) {
        return readNumber(object.get(key), key).doubleValue();
    }

    private static int readInt(JSONObject object, String key) {
        return new BigDecimal(readNumber(object.get(key), key).toString()).intValueExact();
    }

    private static Number readNumber(Object value, String key) {
        if (!(value instanceof Number) || !Double.isFinite(((Number) value).doubleValue())) {
            throw new JSONException(key + " must be a finite number");
        }
        return (Number) value;
    }

    private static Path selectedPath(String filePath) throws SceneFileException {
        try {
            Path path = Path.of(filePath).toAbsolutePath();
            if (filePath.isEmpty() || path.getFileName() == null) {
                throw new SceneFileException("Choose a scene file.");
            }
            return path;
        } catch (InvalidPathException | SecurityException e) {
            throw new SceneFileException("The scene file path is invalid.", e);
        }
    }

    private static boolean writeFile(Path path, String contents, boolean overwrite) throws IOException {
        Path temp = Files.createTempFile(path.getParent(), "scene-", ".tmp");
        try {
            Files.writeString(temp, contents);
            if (!overwrite) {
                try {
                    Files.move(temp, path);
                    return true;
                } catch (FileAlreadyExistsException e) {
                    return false;
                }
            }
            try {
                Files.move(temp, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}

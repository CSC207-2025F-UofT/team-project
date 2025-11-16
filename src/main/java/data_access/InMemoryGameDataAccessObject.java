package data_access;

import entity.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import use_case.game.GameDataAccessInterface;
import use_case.save.SaveOutputData;
import use_case.switch_to_game.SwitchToGameViewDataAccessInterface;
import use_case.save.SaveDataAccessInterface;
import use_case.load.LoadDataAccessInterface;

import java.util.*;

/**
 * In-memory implementation of game data access.
 */
public class InMemoryGameDataAccessObject implements SwitchToGameViewDataAccessInterface, GameDataAccessInterface,
    SaveDataAccessInterface, LoadDataAccessInterface {

    private Scene currentScene;
    private Player player;
    private Map<String, Scene> scenes = new HashMap<>();

    public InMemoryGameDataAccessObject() {
        ClickableObject object1 = new ClickableObjectFactory().create("Object1", 0, 0, "object1.png");
        ClickableObject object2 = new ClickableObjectFactory().create("Object2", 600, 300, "object2.png");
        Scene scene1 = new SceneFactory().create("Scene1", new ArrayList<>(List.of(object1, object2)), "scene1.png");
        Scene scene2 = new SceneFactory().create("Scene2", new ArrayList<>(List.of(object2, object1)), "scene2.png");
        scenes.put("Scene1", scene1);
        scenes.put("Scene2", scene2);
        currentScene = scenes.get("Scene1");
    }

    @Override
    public Scene getCurrentScene() {
        return currentScene;
    }

    public Map<String, Scene> getScenes() {
        return scenes;
    }

    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }

    public Player getPlayer() {
        return player;
    }

    public void saveGame(SaveOutputData outputData) {
        JSONObject gameState = new JSONObject();
        List<Scene> scenesData = new ArrayList<>(outputData.getScenes().values());
        JSONArray scenes = new JSONArray();
        for (Scene s : scenesData) {
            scenes.put(s.toJson());
        }
        gameState.put("scenes", scenes);
        gameState.put("player", outputData.getPlayer().toJson());
        gameState.put("currentScene", outputData.getCurrentScene().toJson());

        // Save to file
        try (FileWriter file = new FileWriter("save.json")) {
            file.write(gameState.toString(4)); // pretty print indent
        } catch (IOException e) {
            System.out.println("Error writing JSON: " + e.getMessage());
        }
    }

    public void loadGame(String filePath) {
        StringBuilder jsonText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading save file: " + e.getMessage());
            return;
        }
        JSONObject gameState = new JSONObject(jsonText.toString());

        JSONObject playerJson = gameState.getJSONObject("player");
        this.player = Player.fromJson(playerJson);

        JSONArray scenesArray = gameState.getJSONArray("scenes");
        Map<String, Scene> loadedScenes = new HashMap<>();
        for (int i = 0; i < scenesArray.length(); i++) {
            Scene s = Scene.fromJson(scenesArray.getJSONObject(i));
            loadedScenes.put(s.getName(), s);
        }
        this.scenes = loadedScenes;

        JSONObject currentSceneJson = gameState.getJSONObject("currentScene");
        this.currentScene = Scene.fromJson(currentSceneJson);
    }

}

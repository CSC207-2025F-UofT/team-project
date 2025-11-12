package data_access;

import entity.*;
import use_case.game.GameDataAccessInterface;
import use_case.switch_to_game.SwitchToGameViewDataAccessInterface;

import java.util.*;

/**
 * In-memory implementation of game data access.
 */
public class InMemoryGameDataAccessObject implements SwitchToGameViewDataAccessInterface, GameDataAccessInterface {

    private Scene currentScene;
    private Player player;
    private Map<String, Scene> scenes = new HashMap<>();

    public InMemoryGameDataAccessObject() {
        ClickableObject object1 = new ClickableObjectFactory().create("Object1", 0, 0, "object1.png",false);
        ClickableObject object2 = new ClickableObjectFactory().create("Object2", 600, 300, "object2.png", false);
        ClickableObject object3 = new ClickableObjectFactory().create("Object3", 200, 200, "object2.png", true);
        this.player = new PlayerFactory().create();
        ArrayList<ClickableObject> sceneOneList = new ArrayList<>(List.of(object1, object2, object3));
        Scene scene1 = new SceneFactory().create("Scene1", sceneOneList, "scene1.png");
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

    public void setPlayer(Player player) {
        this.player = player;
    }
    private final java.util.Set<String> unlockedDoors = new java.util.HashSet<>();

    @Override
    public boolean isDoorUnlocked(String doorName) {
        return unlockedDoors.contains(doorName);
    }

    @Override
    public void unlockDoor(String doorName) {
        unlockedDoors.add(doorName);
    }


}

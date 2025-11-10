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
    private Map<String, DialogueBox> dialogues = new HashMap<>();

    public InMemoryGameDataAccessObject() {
        ClickableObject object1 = new ClickableObjectFactory().create("Object1", 0, 0, "object1.png");
        ClickableObject object2 = new ClickableObjectFactory().create("Object2", 600, 300, "object2.png");
        NonPlayableCharacter npc1 = new NonPlayableCharacterFactory().create("NPC1", 300, 300, "npc1.png", 1);
        Scene scene1 = new SceneFactory().create("Scene1", new ArrayList<>(List.of(object1, object2)), "scene1.png");
        Scene scene2 = new SceneFactory().create("Scene2", new ArrayList<>(List.of(npc1, object2, object1)), "scene2.png");
        ClickableObject db1op1 = new ClickableObjectFactory().create("d1op11", 10, 360, "do1.png");
        ClickableObject db1op2 = new ClickableObjectFactory().create("d1op12", 10, 415, "do2.png");
        ClickableObject db1op3 = new ClickableObjectFactory().create("d1op13", 10, 470, "do3.png");
        ClickableObject db1o11 = new ClickableObjectFactory().create("d1o11", 10, 360, "text1.png");
        ClickableObject db1o21 = new ClickableObjectFactory().create("d1o21", 10, 360, "text2.png");
        ClickableObject db1o31 = new ClickableObjectFactory().create("d1o31", 10, 360, "text3.png");
        ClickableObject db1exit = new ClickableObjectFactory().create("d1e", 10, 525, "exit.png");
        ClickableObject db1return = new ClickableObjectFactory().create("d1r", 10, 525, "return.png");
        DialogueBox db1m = new DialogueBoxFactory().create("DialogueBox1", new ArrayList<>(List.of(db1op1, db1op2, db1op3, db1exit)), "db1.png", 1);
        DialogueBox db1o1 = new DialogueBoxFactory().create("DialogueBoxOption1", new ArrayList<>(List.of(db1o11, db1return)), "db1.png", 1);
        DialogueBox db1o2 = new DialogueBoxFactory().create("DialogueBoxOption2", new ArrayList<>(List.of(db1o21, db1return)), "db1.png", 1);
        DialogueBox db1o3 = new DialogueBoxFactory().create("DialogueBoxOption3", new ArrayList<>(List.of(db1o31, db1return)), "db1.png", 1);
        scenes.put("Scene1", scene1);
        scenes.put("Scene2", scene2);
        scenes.put("DialogueBox1", db1m);
        scenes.put("DialogueBoxOption1", db1o1);
        scenes.put("DialogueBoxOption2", db1o2);
        scenes.put("DialogueBoxOption3", db1o3);
        currentScene = scenes.get("Scene1");
    }

    @Override
    public Scene getCurrentScene() {
        return currentScene;
    }

    public Map<String, Scene> getScenes() {
        return scenes;
    }

    public Map<String, DialogueBox> getDialogues() {return dialogues;}

    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }

    public Player getPlayer() {
        return player;
    }

}

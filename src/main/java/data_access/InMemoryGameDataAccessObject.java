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

    public static int option_xpos = 50;
    public static int option1_ypos = 350;
    int option2_ypos = 400;
    int option3_ypos = 450;
    public static int option4_ypos = 500;
    public static int text_xpos = 250;
    public static int text_ypos = option1_ypos;
    String db1_pic = "db1.png";

    public InMemoryGameDataAccessObject() {
        ClickableObject object1 = new ClickableObjectFactory().create("Object1", 0, 0, "object1.png",false);
        ClickableObject object2 = new ClickableObjectFactory().create("Object2", 600, 300, "object2.png", false);
        ClickableObject object3 = new ClickableObjectFactory().create("Object3", 200, 200, "object2.png", true);
        this.player = new PlayerFactory().create();
        ArrayList<ClickableObject> sceneOneList = new ArrayList<>(List.of(object1, object2, object3));
        Scene scene1 = new SceneFactory().create("Scene1", sceneOneList, "scene1.png");
        Scene scene2 = new SceneFactory().create("Scene2", new ArrayList<>(List.of(object2, object1)), "scene2.png");

        Scene scene1 = new SceneFactory().create("Scene1", new ArrayList<>(List.of(object2)), "scene1.png");
        Scene scene2 = new SceneFactory().create("Scene2", new ArrayList<>(List.of(object1)), "scene2.png");

        DialogueBox db1m1 = new DialogueBoxFactory().create(new ArrayList<>(List.of()), db1_pic);
        DialogueOption db1op11 = new DialogueOptionFactory().create("Ok.", option_xpos, option1_ypos, scene2);

        DialogueBox db1m2 = new DialogueBoxFactory().create(new ArrayList<>(List.of()), db1_pic);
//        DialogueOption db1op21 = new DialogueOptionFactory().create("Ok", option_xpos, option1_ypos, db1m2);

        DialogueBox db1m3 = new DialogueBoxFactory().create(new ArrayList<>(List.of()), db1_pic);
//        DialogueOption db1op31 = new DialogueOptionFactory().create("Ok", option_xpos, option1_ypos, db1m3);

        DialogueOption db1op1 = new DialogueOptionFactory().create("What do you think of me?", option_xpos, option1_ypos, db1m1);
        DialogueOption db1op2 = new DialogueOptionFactory().create("Tell me a story", option_xpos, option2_ypos, db1m2);
        DialogueOption db1op3 = new DialogueOptionFactory().create("Tell me a joke", option_xpos, option3_ypos, db1m3);
        DialogueOption db1exit = new DialogueOptionFactory().create("EXIT", option_xpos, option4_ypos, scene2);

        DialogueText db1txt = new DialogueTextFactory().create("At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis " +
                "praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, " +
                "similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et " +
                "expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat " +
                "facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum " +
                "necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente " +
                "delectus, ut aut reiciendis.", text_xpos, text_ypos);
        DialogueText db11txt = new DialogueTextFactory().create("EWWW YOU SMELL, GET OUT OF HERE YOU SMELLY SMELLING SMELL EW EW EW", text_xpos, text_ypos);
        DialogueText db12txt = new DialogueTextFactory().create("I used to be an adventurer just like you. Then I took an arrow in the knee. ", text_xpos, text_ypos);
        DialogueText db13txt = new DialogueTextFactory().create("There's an old Soviet joke:\nA man goes to a newspaper stand every day, buys a copy of " +
                "Pravda, glances at the front cover, curses, and throws it away.\nAfter a few weeks of this the seller just has to ask what's going on: \"why do " +
                "you always look at the cover but never inside?\"\n\"I'm looking for an obituary.\"\n\"An obituary? But those are in the back!\"\n\"Oh no, the " +
                "obituary I'm looking for will be on the front page.\"", text_xpos, text_ypos);

        DialogueBox db1m = new DialogueBoxFactory().create(new ArrayList<>(List.of(db1op1, db1op2, db1op3, db1exit, db1txt)), db1_pic);

        DialogueOption db1re = new DialogueOptionFactory().create("RETURN", option_xpos, option4_ypos, db1m);

        NonPlayableCharacter npc1 = new NonPlayableCharacterFactory().create("NPC1", 300, 300, "npc1.png", db1m);

        db1m1.addObject(db1op11);
//        db1m2.addObject(db1op21);
//        db1m3.addObject(db1op31);
        db1m1.addObject(db1re);
        db1m1.addObject(db11txt);
        db1m2.addObject(db12txt);
        db1m3.addObject(db13txt);
        db1m2.addObject(db1re);
        db1m3.addObject(db1re);
        scene2.addObject(npc1);

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


    public void save() {

    }

    public void load() {

    }

}

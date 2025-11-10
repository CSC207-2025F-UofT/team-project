package use_case.game;

import entity.ClickableObject;
import entity.Scene;
import use_case.switch_to_game.SwitchToGameOutputData;
import use_case.switch_to_game.SwitchToGameViewDataAccessInterface;

/**
 * The Click Button Interactor.
 */
public class GameInteractor implements GameInputBoundary {
    private final GameOutputBoundary presenter;
    private final GameDataAccessInterface gameDataAccessInterface;

    public GameInteractor(GameDataAccessInterface gameDataAccessInterface, GameOutputBoundary gameOutputBoundary) {
        this.presenter = gameOutputBoundary;
        this.gameDataAccessInterface = gameDataAccessInterface;
    }

    @Override
    public void execute(GameInputData gameInputData) {
        // the clickable object
        ClickableObject clicked = gameInputData.getClickableObject();

        // game logic

        if (clicked.getName().equals("Object1")) {
            gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("Scene1"));
        } else if (clicked.getName().equals("Object2")) {
            gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("Scene2"));
        } else if (clicked.getName().equals("NPC1")) {
            gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("DialogueBox1"));
        } else if  (clicked.getName().equals("d1e")) {
            gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("Scene2"));
        } else if  (clicked.getName().equals("d1op11")) {
            gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("DialogueBoxOption1"));
        } else if  (clicked.getName().equals("d1op12")) {
            gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("DialogueBoxOption2"));
        } else if  (clicked.getName().equals("d1op13")) {
            gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("DialogueBoxOption3"));
        } else if  (clicked.getName().equals("d1r")) {
            gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("DialogueBox1"));
        }

        // update game ui
        Scene currentScene = gameDataAccessInterface.getCurrentScene();
        GameOutputData gameOutputData = new GameOutputData();
        gameOutputData.setBackgroundImage(currentScene.getImage());
        gameOutputData.setClickableObjects(currentScene.getObjects());
        presenter.prepareView(gameOutputData);

    }
}

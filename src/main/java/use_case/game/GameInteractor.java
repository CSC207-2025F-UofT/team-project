package use_case.game;

import entity.ClickableObject;
import entity.DialogueOption;
import entity.NonPlayableCharacter;
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
        if (clicked instanceof NonPlayableCharacter) {
            gameDataAccessInterface.setCurrentScene(((NonPlayableCharacter) clicked).getDB());
        }
        else if (clicked instanceof DialogueOption) {
            gameDataAccessInterface.setCurrentScene(((DialogueOption) clicked).getScene());
        }
        else {
            switch (clicked.getName()) {
                case "Object1":
                    gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("Scene1"));
                    break;
                case "Object2":
                    gameDataAccessInterface.setCurrentScene(gameDataAccessInterface.getScenes().get("Scene2"));
                    break;
            }
        }

        // update game ui
        Scene currentScene = gameDataAccessInterface.getCurrentScene();
        GameOutputData gameOutputData = new GameOutputData();
        gameOutputData.setBackgroundImage(currentScene.getImage());
        gameOutputData.setClickableObjects(currentScene.getObjects());
        presenter.prepareView(gameOutputData);

    }
}

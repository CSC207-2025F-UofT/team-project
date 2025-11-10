package use_case.game;

import entity.ClickableObject;
import entity.Scene;
import use_case.switch_to_game.SwitchToGameOutputData;
import use_case.switch_to_game.SwitchToGameViewDataAccessInterface;

import javax.swing.*;

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
        } else if (clicked.getName().equals("Object3")) {
            // 1) Decide which scene we’re updating (here: the current scene)
            Scene cur = gameDataAccessInterface.getCurrentScene();

            // 2) Make a mutable copy of the objects and remove Object3
            java.util.List<entity.ClickableObject> updatedObjects = new java.util.ArrayList<>(cur.getObjects());
            updatedObjects.removeIf(o -> "Object3".equals(o.getName()));

            // 3) Build a new Scene (Scene is immutable in your model)
            entity.Scene updatedScene = new entity.Scene(
                    cur.getName(),
                    updatedObjects,
                    cur.getImage()
            );

            // 4) Put the updated scene back into the DAO and set it current
            gameDataAccessInterface.getScenes().put(updatedScene.getName(), updatedScene);
            gameDataAccessInterface.setCurrentScene(updatedScene);
            javax.swing.SwingUtilities.invokeLater(() ->
                    javax.swing.JOptionPane.showMessageDialog(
                            null,                       // parent; null centers on screen
                            "You collected Object 3!",  // message
                            "Item Collected",           // title
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                    )
            );
            // (Optional) If collecting should also switch scenes, do it before step 3/4 instead.
        }


        // update game ui
        Scene currentScene = gameDataAccessInterface.getCurrentScene();
        GameOutputData gameOutputData = new GameOutputData();
        gameOutputData.setBackgroundImage(currentScene.getImage());
        gameOutputData.setClickableObjects(currentScene.getObjects());
        presenter.prepareView(gameOutputData);

    }
}

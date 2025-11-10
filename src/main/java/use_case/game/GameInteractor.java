package use_case.game;

import entity.ClickableObject;
import entity.Scene;

import javax.swing.*; // for optional popup
import java.util.Objects;

public class GameInteractor implements GameInputBoundary {
    private final GameOutputBoundary presenter;
    private final GameDataAccessInterface gameDataAccessInterface;
    private final GameManager gameManager;

    public GameInteractor(GameDataAccessInterface gameDataAccessInterface,
                          GameOutputBoundary gameOutputBoundary,
                          GameManager gameManager) {
        this.presenter = Objects.requireNonNull(gameOutputBoundary);
        this.gameDataAccessInterface = Objects.requireNonNull(gameDataAccessInterface);
        this.gameManager = Objects.requireNonNull(gameManager);
    }

    @Override
    public void execute(GameInputData gameInputData) {
        // 1) Get clicked & rule
        ClickableObject clicked = gameInputData.getClickableObject();
        ClickRule rule = gameManager.ruleFor(clicked);

        // 2) Start from current scene
        Scene cur = gameDataAccessInterface.getCurrentScene();

        // 3) Apply collection/removal to current scene (immutably)
        Scene updatedCur = gameManager.applyToCurrentScene(rule, cur, clicked);
        gameDataAccessInterface.getScenes().put(updatedCur.getName(), updatedCur);
        gameDataAccessInterface.setCurrentScene(updatedCur);

        // 4) Handle scene change if the rule asks for it
        if (rule.getType() == ClickActionType.CHANGE_SCENE
                || rule.getType() == ClickActionType.COLLECT_AND_CHANGE_SCENE) {
            rule.getTargetScene().ifPresent(sceneName -> {
                Scene target = gameDataAccessInterface.getScenes().get(sceneName);
                if (target != null) {
                    gameDataAccessInterface.setCurrentScene(target);
                }
            });
        }

        // 5) Optional: show a popup/message if provided
        rule.getMessage().ifPresent(msg ->
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(
                                null, msg, "Info", JOptionPane.INFORMATION_MESSAGE
                        )));

        // 6) Refresh UI via presenter (unchanged from your flow)
        Scene currentScene = gameDataAccessInterface.getCurrentScene();
        GameOutputData out = new GameOutputData();
        out.setBackgroundImage(currentScene.getImage());
        out.setClickableObjects(currentScene.getObjects());
        presenter.prepareView(out);
    }
}


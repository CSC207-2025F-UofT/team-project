package app;

import data_access.InMemoryGameDataAccessObject;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.game.GameController;
import interface_adapter.game.GamePresenter;
import interface_adapter.game.GameViewModel;
import interface_adapter.main_menu.MainMenuController;
import interface_adapter.main_menu.MainMenuPresenter;
import interface_adapter.main_menu.MainMenuViewModel;
import use_case.game.GameInputBoundary;
import use_case.game.GameInteractor;
import use_case.game.GameOutputBoundary;
import use_case.switch_to_game.SwitchToGameViewInputBoundary;
import use_case.switch_to_game.SwitchToGameViewInteractor;
import use_case.switch_to_game.SwitchToGameViewOutputBoundary;
import view.GameView;
import view.MainMenuView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // Create initial game data
    final SceneFactory sceneFactory = new SceneFactory();
    final PlayerFactory playerFactory = new PlayerFactory();
    final InMemoryGameDataAccessObject gameDataAccessObject = new InMemoryGameDataAccessObject();

    private MainMenuView mainMenuView;
    private MainMenuViewModel mainMenuViewModel;
    private GameViewModel gameViewModel;
    private GameView gameView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addMainMenuView() {
        mainMenuViewModel = new MainMenuViewModel();
        mainMenuView = new MainMenuView(mainMenuViewModel);
        cardPanel.add(mainMenuView, mainMenuView.getViewName());
        return this;
    }

    public AppBuilder addGameView() {
        gameViewModel = new GameViewModel();
        gameView = new GameView(gameViewModel);
        cardPanel.add(gameView, gameView.getViewName());
        return this;
    }

    public AppBuilder addSwitchToGameUseCase() {
        final SwitchToGameViewOutputBoundary switchOutputBoundary =
                new MainMenuPresenter(viewManagerModel, gameViewModel);
        final SwitchToGameViewInputBoundary switchInteractor =
                new SwitchToGameViewInteractor(gameDataAccessObject, switchOutputBoundary);

        MainMenuController controller = new MainMenuController(switchInteractor);
        mainMenuView.setMainMenuController(controller);
        return this;
    }

    public AppBuilder addClickButtonUseCase() {
        final GameOutputBoundary gameOutputBoundary =
                new GamePresenter(gameViewModel);
        final GameInputBoundary clickButtonInteractor =
                new GameInteractor(gameDataAccessObject, gameOutputBoundary);

        GameController gameController = new GameController(clickButtonInteractor);
        gameView.setGameController(gameController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Point and Click Game");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);
        application.setPreferredSize(new Dimension(800, 600));

        viewManagerModel.setState(mainMenuView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}

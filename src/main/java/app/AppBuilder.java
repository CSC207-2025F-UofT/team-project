package app;

import data_access.InMemoryGameStateDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.hud.HudViewModel;
import interface_adapter.new_game.NewGameController;
import interface_adapter.new_game.NewGamePresenter;
import use_case.GameStateDataAccessInterface;
import use_case.new_game.NewGameInputBoundary;
import use_case.new_game.NewGameInteractor;
import use_case.new_game.NewGameOutputBoundary;
import view.GameView;
import view.StartView;
import view.ViewManager;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Wires the app together (Dependency Injection). This is the only class
 * that knows every concrete class; each story adds its own add...View()
 * and add...UseCase() methods here.
 *
 * Call order: add views first, then use cases, then build().
 */
public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager;

    private final GameStateDataAccessInterface gameStateDataAccessObject =
            new InMemoryGameStateDataAccessObject();

    private HudViewModel hudViewModel;
    private GameView gameView;
    private StartView startView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    }

    public AppBuilder addGameView() {
        hudViewModel = new HudViewModel();
        gameView = new GameView(hudViewModel);
        cardPanel.add(gameView, gameView.getViewName());
        return this;
    }

    /** Adds the New Game use case and its temporary start screen. Requires addGameView(). */
    public AppBuilder addNewGameUseCase() {
        NewGameOutputBoundary presenter =
                new NewGamePresenter(hudViewModel, viewManagerModel, gameView.getViewName());
        NewGameInputBoundary interactor = new NewGameInteractor(gameStateDataAccessObject, presenter);
        NewGameController controller = new NewGameController(interactor);

        startView = new StartView(controller);
        cardPanel.add(startView, startView.getViewName());
        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("UofTears: Survive the Semester");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(startView.getViewName());
        viewManagerModel.firePropertyChanged();
        return application;
    }
}

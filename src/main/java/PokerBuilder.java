
import javax.swing.*;
import java.awt.*;

import main.interface_adapters.GameViewModel;
import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.main_menu.*;
import main.view.*;
import main.view.ViewManager;


public class PokerBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private MainMenuView mainMenuView;
    private MainMenuViewModel mainMenuViewModel;

    private GameView gameView;
    private GameViewModel gameViewModel;


    public PokerBuilder() {cardPanel.setLayout(cardLayout);}

    public PokerBuilder addMainMenuView() {
        // Creates new Main Menu View and sets it to display
        this.mainMenuViewModel = new MainMenuViewModel();
        this.mainMenuView = new MainMenuView(mainMenuViewModel);

        MainMenuPresenter mainMenuPresenter = new MainMenuPresenter(viewManagerModel,gameViewModel);
        MainMenuController mainMenuController = new MainMenuController(mainMenuPresenter);
        mainMenuView.setMainMenuController(mainMenuController);

        cardPanel.add(mainMenuView, MainMenuViewModel.VIEW_NAME);
        return this;
    }

    public PokerBuilder addGameView() {
        this.gameViewModel = new GameViewModel();
        this.gameView = new GameView(gameViewModel);

        cardPanel.add(gameView, GameViewModel.VIEW_NAME);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Poker");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        viewManagerModel.setActiveView(MainMenuViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
        application.add(cardPanel);

        return application;
    }
}

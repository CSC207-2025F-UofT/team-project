
import javax.swing.*;
import java.awt.*;

import main.interface_adapters.game.GameViewModel;
import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.main_menu.*;
import main.interface_adapters.settings.SettingsViewModel;
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

    private SettingsView settingsView;
    private SettingsViewModel settingsViewModel;


    public PokerBuilder() {cardPanel.setLayout(cardLayout);}

    public PokerBuilder addMainMenuView() {
        // Creates new Main Menu View and sets it to display
        this.mainMenuViewModel = new MainMenuViewModel();
        this.mainMenuView = new MainMenuView(mainMenuViewModel);

        MainMenuPresenter mainMenuPresenter = new MainMenuPresenter(
                viewManagerModel, gameViewModel, settingsViewModel);
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

    public PokerBuilder addSettingsView() {
        this.settingsViewModel = new SettingsViewModel();
        this.settingsView = new SettingsView(settingsViewModel);

        cardPanel.add(settingsView, SettingsViewModel.VIEW_NAME);
        return this;
    }

    public JFrame build() {
        //Creates frame
        final JFrame application = new JFrame("Poker");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Tells JSwing to display the main menu
        viewManagerModel.setActiveView(MainMenuViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        application.add(cardPanel);


        return application;
    }
}


import javax.swing.*;
import java.awt.*;

import main.interface_adapters.game.GameViewModel;
import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.lobby.LobbyController;
import main.interface_adapters.lobby.LobbyPresenter;
import main.interface_adapters.lobby.LobbyViewModel;
import main.interface_adapters.main_menu.*;
import main.interface_adapters.settings.SettingsController;
import main.interface_adapters.settings.SettingsPresenter;
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

    private LobbyView lobbyView;
    private LobbyViewModel lobbyViewModel;

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
                viewManagerModel, lobbyViewModel, settingsViewModel);
        MainMenuController mainMenuController = new MainMenuController(mainMenuPresenter);
        mainMenuView.setController(mainMenuController);

        cardPanel.add(mainMenuView, MainMenuViewModel.VIEW_NAME);
        return this;
    }

    public PokerBuilder addLobbyView() {
        this.lobbyViewModel = new LobbyViewModel();
        this.lobbyView = new LobbyView(lobbyViewModel);

        LobbyPresenter lobbyPresenter = new LobbyPresenter(viewManagerModel, lobbyViewModel,
                mainMenuViewModel, gameViewModel);
        LobbyController lobbyController = new LobbyController(lobbyPresenter);
        lobbyView.setController(lobbyController);

        cardPanel.add(lobbyView, LobbyViewModel.VIEW_NAME);
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

        SettingsPresenter settingsPresenter = new SettingsPresenter(viewManagerModel,
                settingsViewModel, mainMenuViewModel);
        SettingsController settingsController = new SettingsController(settingsPresenter);
        settingsView.setController(settingsController);

        cardPanel.add(settingsView, SettingsViewModel.VIEW_NAME);
        return this;
    }

    public JFrame build() {
        //Creates frame
        final JFrame application = new JFrame("Poker");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setUndecorated(true);
        application.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Tells JSwing to display the main menu
        viewManagerModel.setActiveView(MainMenuViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        application.add(cardPanel);


        return application;
    }
}

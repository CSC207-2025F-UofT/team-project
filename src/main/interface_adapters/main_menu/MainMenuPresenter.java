package main.interface_adapters.main_menu;

import main.interface_adapters.game.GameViewModel;
import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.lobby.LobbyViewModel;
import main.interface_adapters.settings.SettingsViewModel;

// implements MainMenuOutputBoundary
public class MainMenuPresenter {

    private final ViewManagerModel viewManagerModel;
    private final LobbyViewModel lobbyViewModel;
    private final SettingsViewModel settingsViewModel;

    public MainMenuPresenter(ViewManagerModel viewManagerModel, LobbyViewModel lobbyViewModel,
                             SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.lobbyViewModel = lobbyViewModel;
        this.settingsViewModel = settingsViewModel;
    }

    public void switchToLobbyView() {
        this.viewManagerModel.setActiveView(LobbyViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }

    public void switchToSettingsView() {
        this.viewManagerModel.setActiveView(SettingsViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }

    public void exitApplication() {
        viewManagerModel.setActiveView("exit");
        viewManagerModel.notifyViewChanged();
    }
}

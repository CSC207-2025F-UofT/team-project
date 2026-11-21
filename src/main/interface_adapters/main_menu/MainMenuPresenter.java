package main.interface_adapters.main_menu;

import main.interface_adapters.game.GameViewModel;
import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.settings.SettingsViewModel;

// implements MainMenuOutputBoundary
public class MainMenuPresenter {

    private final ViewManagerModel viewManagerModel;
    private final GameViewModel gameViewModel;
    private final SettingsViewModel settingsViewModel;

    public MainMenuPresenter(ViewManagerModel viewManagerModel, GameViewModel gameViewModel,
                             SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.gameViewModel = gameViewModel;
        this.settingsViewModel = settingsViewModel;
    }

    public void switchToGameView() {
        this.viewManagerModel.setActiveView(GameViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }

    public void switchToSettingsView() {
        this.viewManagerModel.setActiveView(SettingsViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }
}

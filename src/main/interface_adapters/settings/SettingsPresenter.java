package main.interface_adapters.settings;

import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.main_menu.MainMenuViewModel;

public class SettingsPresenter {

    private final ViewManagerModel viewManagerModel;
    private final SettingsViewModel settingsViewModel;
    private final MainMenuViewModel mainMenuViewModel;

    public SettingsPresenter(ViewManagerModel viewManagerModel, SettingsViewModel settingsViewModel,
    MainMenuViewModel mainMenuViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.mainMenuViewModel = mainMenuViewModel;
    }

    public void switchToMainMenuView() {
        viewManagerModel.setActiveView(MainMenuViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}

package main.interface_adapters.settings;

public class SettingsController {

    private final SettingsPresenter settingsPresenter;

    public SettingsController(SettingsPresenter settingsPresenter) {
        this.settingsPresenter = settingsPresenter;
    }

    public void switchToMainMenuView() {
        settingsPresenter.switchToMainMenuView();
    }
}

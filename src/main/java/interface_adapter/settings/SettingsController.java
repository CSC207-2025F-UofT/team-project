package interface_adapter.settings;

import use_case.settings.SettingsInputBoundary;

public class SettingsController {
    private final SettingsInputBoundary inputBoundary;

    public SettingsController(SettingsInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void goBack() {
        inputBoundary.goBack();
    }
}

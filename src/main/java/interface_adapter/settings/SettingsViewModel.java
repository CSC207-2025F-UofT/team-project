package interface_adapter.settings;

import interface_adapter.ViewModel;

/**
 * The View Model for the Settings View.
 * Uses the team's shared ViewModel, so ViewManager can switch to it by VIEW_NAME.
 */
public class SettingsViewModel extends ViewModel<SettingsState> {

    public static final String VIEW_NAME = "Settings";

    public SettingsViewModel() {
        super(VIEW_NAME, SettingsState.empty());
    }
}

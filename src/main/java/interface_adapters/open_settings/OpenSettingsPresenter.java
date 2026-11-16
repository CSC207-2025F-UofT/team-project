package interface_adapters.open_settings;

import interface_adapters.update_timer_settings.TimerSettingsState;
import interface_adapters.update_timer_settings.TimerSettingsViewModel;
import use_case.open_settings.OpenSettingsOutputBoundary;

/**
 * The Presenter for the Open Settings Use Case.
 */
public class OpenSettingsPresenter implements OpenSettingsOutputBoundary {
    private final TimerSettingsViewModel viewModel;

    public OpenSettingsPresenter(TimerSettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Prepares the Open Timer Settings View Model for a successful execution of the use case.
     */
    @Override
    public void prepareSuccessView() {
        TimerSettingsState state = viewModel.getState();
        state.setSettingsOpen(true);
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

}

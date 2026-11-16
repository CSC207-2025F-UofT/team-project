package interface_adapters.update_timer_settings;

import interface_adapters.home.HomeState;
import interface_adapters.home.HomeViewModel;
import use_case.update_timer_settings.UpdateTimerSettingsOutputBoundary;
import use_case.update_timer_settings.UpdateTimerSettingsOutputData;

/**
 * The Presenter for the Update Timer Settings Use Case.
 */
public class UpdateTimerSettingsPresenter implements UpdateTimerSettingsOutputBoundary {

    private final TimerSettingsViewModel sessionViewModel;
    private final HomeViewModel homeViewModel;

    public UpdateTimerSettingsPresenter(TimerSettingsViewModel sessionViewModel, HomeViewModel homeViewModel) {
        this.sessionViewModel = sessionViewModel;
        this.homeViewModel = homeViewModel;
    }

    /**
     * Prepares the Update Timer Settings View Model for a successful execution of the use case.
     * @param outputData the output data returned by the Update Timer Settings Interactor
     */
    @Override
    public void prepareSuccessView(UpdateTimerSettingsOutputData outputData) {
        HomeState homeState = homeViewModel.getState();
        homeState.setTimerDisplay(String.format("%02d:00", outputData.getFocusDuration()));
        homeViewModel.setState(homeState);
        homeViewModel.notifyStateChanged();

        TimerSettingsState sessionState = sessionViewModel.getState();
        sessionState.setErrorMessage(null);
        sessionViewModel.setState(sessionState);
        sessionViewModel.notifyStateChanged();
    }

    /**
     * Prepares the Update Timer Settings View Model for a failed execution of the use case.
     * @param errorMessage the error message describing why the use case failed
     */
    @Override
    public void prepareFailView(String errorMessage) {
        TimerSettingsState state = sessionViewModel.getState();
        state.setErrorMessage(errorMessage);
        sessionViewModel.setState(state);
        sessionViewModel.notifyStateChanged();
    }
}

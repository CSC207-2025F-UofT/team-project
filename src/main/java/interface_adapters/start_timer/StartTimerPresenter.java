package interface_adapters.start_timer;

import interface_adapters.home.HomeState;
import interface_adapters.home.HomeViewModel;
import use_case.start_timer.StartTimerOutputBoundary;
import use_case.start_timer.StartTimerOutputData;

/**
 * The Presenter for the Start Timer Use Case.
 */
public class StartTimerPresenter implements StartTimerOutputBoundary {
    private final HomeViewModel homeViewModel;

    public StartTimerPresenter(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
    }

    /**
     * Prepares the Start Timer View Model for a successful execution of the use case.
     */
    @Override
    public void prepareSuccessView(StartTimerOutputData outputData) {
        HomeState state = homeViewModel.getState();

        int timerMinutes = outputData.getDurationMinutes();
        String timerString = String.format("%02d:00", timerMinutes);

        state.setTimerDisplay(timerString);
        state.setTotalSeconds(timerMinutes * 60);
        state.setTimerRunning(true);

        homeViewModel.setState(state);
        homeViewModel.notifyStateChanged();
    }

    /**
     * Updates the timer display during the countdown.
     * @param timerString the remaining time formatted as a string
     */
    @Override
    public void updateTimerCountdown(String timerString) {
        HomeState state = homeViewModel.getState();
        state.setTimerDisplay(timerString);
        homeViewModel.setState(state);

        homeViewModel.notifyStateChanged();
    }

    /**
     * Prepares the Start Timer View Model when the timer finishes running.
     */
    @Override
    public void prepareTimerFinishedView() {
        HomeState state = homeViewModel.getState();
        state.setTimerRunning(false);
        state.setTimerDisplay("00:00");

        homeViewModel.setState(state);
        homeViewModel.notifyStateChanged();
    }
}

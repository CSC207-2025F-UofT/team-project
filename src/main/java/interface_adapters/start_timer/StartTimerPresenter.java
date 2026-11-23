package interface_adapters.start_timer;

import interface_adapters.home.HomeState;
import interface_adapters.home.HomeViewModel;
import interface_adapters.menu_bar.NavigationBarViewModel;
import use_case.start_timer.StartTimerOutputBoundary;
import use_case.start_timer.StartTimerOutputData;

/**
 * The Presenter for the Start Timer Use Case.
 */
public class StartTimerPresenter implements StartTimerOutputBoundary {
    private final HomeViewModel homeViewModel;
    private final NavigationBarViewModel navigationBarViewModel;

    public StartTimerPresenter(HomeViewModel homeViewModel,
                               NavigationBarViewModel navigationBarViewModel) {
        this.homeViewModel = homeViewModel;
        this.navigationBarViewModel = navigationBarViewModel;
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

        // Notifies the Navigation Bar View Model that the timer is currently running.
        navigationBarViewModel.getState().setTimerRunning(true);
        navigationBarViewModel.notifyStateChanged();
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

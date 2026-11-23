package interface_adapters.cancel_timer;

import interface_adapters.home.HomeState;
import interface_adapters.home.HomeViewModel;
import interface_adapters.menu_bar.NavigationBarViewModel;
import use_case.cancel_timer.CancelTimerOutputBoundary;

/**
 * The Presenter for the Cancel Timer Use Case.
 */
public class CancelTimerPresenter implements CancelTimerOutputBoundary {

    private final HomeViewModel homeViewModel;
    private final NavigationBarViewModel navigationBarViewModel;

    public CancelTimerPresenter(HomeViewModel homeViewModel,
                                NavigationBarViewModel navigationBarViewModel) {
        this.homeViewModel = homeViewModel;
        this.navigationBarViewModel = navigationBarViewModel;
    }

    /**
     * Prepares the Cancel Timer View Model for a successful execution of the use case.
     */
    @Override
    public void prepareSuccessView() {
        HomeState state = homeViewModel.getState();
        state.setTimerRunning(false);
        state.setTimerDisplay("00:00");

        homeViewModel.setState(state);
        homeViewModel.notifyStateChanged();

        // Notifies the Navigation Bar View Model that the timer has been cancelled.
        navigationBarViewModel.getState().setTimerRunning(false);
        navigationBarViewModel.notifyStateChanged();
    }
}

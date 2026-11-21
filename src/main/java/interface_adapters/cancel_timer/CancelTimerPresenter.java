package interface_adapters.cancel_timer;

import interface_adapters.home.HomeState;
import interface_adapters.home.HomeViewModel;
import use_case.cancel_timer.CancelTimerOutputBoundary;

/**
 * The Presenter for the Cancel Timer Use Case.
 */
public class CancelTimerPresenter implements CancelTimerOutputBoundary {

    private final HomeViewModel homeViewModel;

    public CancelTimerPresenter(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
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
    }
}

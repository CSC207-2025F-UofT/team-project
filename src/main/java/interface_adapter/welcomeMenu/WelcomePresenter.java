package interface_adapter.welcomeMenu;

import interface_adapter.start_round.StartRoundViewModel;
import interface_adapter.start_round.StartRoundState;
import use_case.welcomeMenu.WelcomeOutputBoundary;
import use_case.welcomeMenu.WelcomeOutputData;

public class WelcomePresenter implements WelcomeOutputBoundary {
    private final WelcomeViewModel welcomeViewModel;
    private final StartRoundViewModel startRoundViewModel;
    private final ViewManagerModel viewManagerModel;

    public WelcomePresenter(ViewManagerModel viewManagerModel,
                          StartRoundViewModel startRoundViewModel,
                          WelcomeViewModel welcomeViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.startRoundViewModel = startRoundViewModel;
        this.welcomeViewModel = welcomeViewModel;
    }

    @Override
    public void prepareSuccessView(WelcomeOutputData response) {
        // On success, update the loggedInViewModel's state
        final StartRoundState startRoundState = startRoundViewModel.getState();
        startRoundState.setUsername(response.getUsername());
        this.startRoundViewModel.notifyStateChanged();

        // and clear everything from the welcomeViewModel's state
        this.welcomeViewModel.setState(new WelcomeState());
        this.welcomeViewModel.notifyStateChanged();

        // switch to the logged in view
        this.viewManagerModel.setState(StartRoundViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final WelcomeState welcomeState = welcomeViewModel.getState();
        welcomeState.setWelcomeError(error);
        welcomeViewModel.notifyStateChanged();
    }
}

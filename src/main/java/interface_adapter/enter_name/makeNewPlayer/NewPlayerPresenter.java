package interface_adapter.enter_name.makeNewPlayer;

import interface_adapter.ViewManagerModel;
import interface_adapter.enter_name.welcomeMenu.WelcomeState;
import interface_adapter.enter_name.welcomeMenu.WelcomeViewModel;
import use_case.enter_name.makeNewPlayer.NewPlayerOutputBoundary;
import use_case.enter_name.makeNewPlayer.NewPlayerOutputData;

public class NewPlayerPresenter implements NewPlayerOutputBoundary{
    private final NewPlayerViewModel newPlayerViewModel;
    private final WelcomeViewModel welcomeViewModel;
    private final ViewManagerModel viewManagerModel;

    public NewPlayerPresenter(ViewManagerModel viewManagerModel,
                           NewPlayerViewModel newPlayerViewModel,
                           WelcomeViewModel welcomeViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.newPlayerViewModel = newPlayerViewModel;
        this.welcomeViewModel = welcomeViewModel;
    }

    @Override
    public void prepareSuccessView(NewPlayerOutputData response) {
        // On success, switch to the login view.
        final WelcomeState welcomeState = welcomeViewModel.getState();
        welcomeState.setUsername(response.getUsername());
        welcomeViewModel.notifyStateChanged();

        viewManagerModel.setState(WelcomeViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final NewPlayerState newPlayerState = newPlayerViewModel.getState();
        newPlayerState.setUsernameError(error);
        newPlayerViewModel.notifyStateChanged();
    }

    @Override
    public void switchToWelcomeView() {
        viewManagerModel.setState(WelcomeViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

}

package interface_adapter.make_new_player;

import interface_adapter.ViewManagerModel;
import interface_adapter.welcome_menu.WelcomeState;
import interface_adapter.welcome_menu.WelcomeViewModel;
import use_case.make_new_player.NewPlayerOutputBoundary;
import use_case.make_new_player.NewPlayerOutputData;

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

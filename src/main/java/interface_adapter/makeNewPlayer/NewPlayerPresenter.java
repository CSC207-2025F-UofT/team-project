package interface_adapter.makeNewPlayer;

//import interface_adapter.ViewManagerModel;
//import interface_adapter.welcome.WelcomeState;
//import interface_adapter.welcome.WelcomeViewModel;
import use_case.makeNewPlayer.NewPlayerOutputBoundary;
import use_case.makeNewPlayer.NewPlayerOutputData;

public class NewPlayerPresenter implements NewPlayerOutputBoundary{
    private final NewPlayerViewModel newPlayerViewModel;
    //private final MainMenuViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public NewPlayerPresenter(ViewManagerModel viewManagerModel,
                           SignupViewModel signupViewModel//,
                           //LoginViewModel loginViewModel
    ) {
        this.viewManagerModel = viewManagerModel;
        this.newPlayerViewModel = newPlayerViewModel;
        //this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(NewPlayerOutputData response) {
        // On success, switch to the login view.
        final WelcomeState welcomeState = WelcomeViewModel.getState();
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
    public void switchToLoginView() {
        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

}

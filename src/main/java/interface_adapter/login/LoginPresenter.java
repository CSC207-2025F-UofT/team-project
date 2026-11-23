package interface_adapter.login;

import entity.User.Role;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.manage_pet.ManagePetViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Creates the presenter for login and the home screen transition.
     * @param viewManagerModel switches screens after login
     * @param loginViewModel displays login feedback
     */
    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // and clear everything from the LoginViewModel's state
        this.loginViewModel.setState(new LoginState());
        this.loginViewModel.notifyStateChanged();

        final String homeView = response.getRole() == Role.ADMIN
                ? ManagePetViewModel.VIEW_NAME : GalleryViewModel.VIEW_NAME;
        this.viewManagerModel.setActiveView(homeView);
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.notifyStateChanged();
    }
}

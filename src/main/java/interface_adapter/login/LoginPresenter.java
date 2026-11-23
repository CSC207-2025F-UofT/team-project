package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    // TODO: replace with GalleryView.VIEW_NAME once feature/a-browse-skeleton is merged.
    private static final String GALLERY_VIEW_NAME = "gallery";

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

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

        // switch to the pet gallery
        this.viewManagerModel.setState(GALLERY_VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.notifyStateChanged();
    }
}

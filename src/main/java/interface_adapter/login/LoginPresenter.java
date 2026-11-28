package interface_adapter.login;

import entity.User.Role;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.manage_pet.ManagePetViewModel;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.track_applications.TrackApplicationsViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SubmitApplicationViewModel submitApplicationViewModel;
    private final TrackApplicationsViewModel trackApplicationsViewModel;

    /**
     * Creates the presenter for login and the home screen transition.
     * @param viewManagerModel switches screens after login
     * @param loginViewModel displays login feedback
     * @param submitApplicationViewModel the application form, told who the adopter is
     * @param trackApplicationsViewModel My Applications, told who the adopter is
     */
    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoginViewModel loginViewModel,
                          SubmitApplicationViewModel submitApplicationViewModel,
                          TrackApplicationsViewModel trackApplicationsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.submitApplicationViewModel = submitApplicationViewModel;
        this.trackApplicationsViewModel = trackApplicationsViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // and clear everything from the LoginViewModel's state
        this.loginViewModel.setState(new LoginState());
        this.loginViewModel.notifyStateChanged();

        // Only adopters apply for pets; an admin login clears any adopter left from before.
        final String adopterId;
        if (response.getRole() == Role.ADMIN) {
            adopterId = "";
        }
        else {
            adopterId = response.getId();
        }
        submitApplicationViewModel.getState().setAdopterId(adopterId);
        trackApplicationsViewModel.getState().setAdopterId(adopterId);

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

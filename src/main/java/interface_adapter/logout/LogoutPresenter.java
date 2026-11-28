package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.track_applications.TrackApplicationsViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Log Out Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;
    private final SubmitApplicationViewModel submitApplicationViewModel;
    private final TrackApplicationsViewModel trackApplicationsViewModel;

    /**
     * Creates the presenter.
     * @param viewManagerModel switches to the public pet gallery
     * @param loginViewModel the login screen, cleared for the next user
     * @param submitApplicationViewModel the application form, which forgets the adopter
     * @param trackApplicationsViewModel My Applications, which forgets the adopter
     */
    public LogoutPresenter(ViewManagerModel viewManagerModel,
                           LoginViewModel loginViewModel,
                           SubmitApplicationViewModel submitApplicationViewModel,
                           TrackApplicationsViewModel trackApplicationsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.submitApplicationViewModel = submitApplicationViewModel;
        this.trackApplicationsViewModel = trackApplicationsViewModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData outputData) {
        loginViewModel.setState(new LoginState());
        loginViewModel.notifyStateChanged();

        submitApplicationViewModel.getState().setAdopterId("");
        trackApplicationsViewModel.getState().setAdopterId("");

        viewManagerModel.setActiveView(GalleryViewModel.VIEW_NAME);
    }
}

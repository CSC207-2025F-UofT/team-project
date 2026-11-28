package interface_adapter.logout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import interface_adapter.ViewManagerModel;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.track_applications.TrackApplicationsViewModel;
import org.junit.jupiter.api.Test;
import use_case.logout.LogoutOutputData;

class LogoutPresenterTest {

    @Test
    void clearsTheAdopterAndShowsTheGallery() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setLoginError("Wrong password");
        final SubmitApplicationViewModel submitViewModel = new SubmitApplicationViewModel();
        submitViewModel.getState().setAdopterId("u1");
        final TrackApplicationsViewModel trackViewModel = new TrackApplicationsViewModel();
        trackViewModel.getState().setAdopterId("u1");
        viewManagerModel.setActiveView("manage pets");

        new LogoutPresenter(viewManagerModel, loginViewModel, submitViewModel, trackViewModel)
                .prepareSuccessView(new LogoutOutputData(true));

        assertEquals("", submitViewModel.getState().getAdopterId());
        assertEquals("", trackViewModel.getState().getAdopterId());
        assertNull(loginViewModel.getState().getLoginError());
        assertEquals(GalleryViewModel.VIEW_NAME, viewManagerModel.getActiveView());
    }
}

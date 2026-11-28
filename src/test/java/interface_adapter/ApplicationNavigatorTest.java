package interface_adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.track_applications.TrackApplicationsViewModel;

class ApplicationNavigatorTest {

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final SubmitApplicationViewModel submitViewModel = new SubmitApplicationViewModel();
    private final TrackApplicationsViewModel trackViewModel = new TrackApplicationsViewModel();
    private final ApplicationNavigator navigator =
            new ApplicationNavigator(viewManagerModel, submitViewModel, trackViewModel);

    @Test
    void adopterIsSharedByBothScreens() {
        navigator.setAdopter("bob");
        assertEquals("bob", submitViewModel.getState().getAdopterId());
        assertEquals("bob", trackViewModel.getState().getAdopterId());
    }

    @Test
    void startApplicationOpensTheFormForThePet() {
        submitViewModel.getState().setSuccessMessage("old message");
        navigator.startApplication("mochi", "Mochi");

        assertEquals("mochi", submitViewModel.getState().getPetId());
        assertEquals("Mochi", submitViewModel.getState().getPetName());
        assertEquals("", submitViewModel.getState().getSuccessMessage());
        assertEquals(SubmitApplicationViewModel.VIEW_NAME, viewManagerModel.getActiveView());
    }

    @Test
    void showMyApplicationsSwitchesScreen() {
        navigator.showMyApplications();
        assertEquals(TrackApplicationsViewModel.VIEW_NAME, viewManagerModel.getActiveView());
    }
}

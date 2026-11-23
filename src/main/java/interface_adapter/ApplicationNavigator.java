package interface_adapter;

import interface_adapter.submit_application.SubmitApplicationState;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.track_applications.TrackApplicationsViewModel;

/**
 * Opens the application screens. Other screens call this: login sets the adopter,
 * and the gallery opens the form for the chosen pet.
 */
public final class ApplicationNavigator {

    private final ViewManagerModel viewManagerModel;
    private final SubmitApplicationViewModel submitApplicationViewModel;
    private final TrackApplicationsViewModel trackApplicationsViewModel;

    /**
     * Creates the navigator.
     * @param viewManagerModel the model that chooses the visible screen
     * @param submitApplicationViewModel the view model of the application form
     * @param trackApplicationsViewModel the view model of My Applications
     */
    public ApplicationNavigator(ViewManagerModel viewManagerModel,
                                SubmitApplicationViewModel submitApplicationViewModel,
                                TrackApplicationsViewModel trackApplicationsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.submitApplicationViewModel = submitApplicationViewModel;
        this.trackApplicationsViewModel = trackApplicationsViewModel;
    }

    /**
     * Remembers who is logged in, so both screens use the same adopter.
     * @param adopterId the logged-in adopter's id
     */
    public void setAdopter(String adopterId) {
        submitApplicationViewModel.getState().setAdopterId(adopterId);
        trackApplicationsViewModel.getState().setAdopterId(adopterId);
    }

    /**
     * Opens the application form for one pet.
     * @param petId the pet to apply for
     * @param petName the pet's name, shown in the form title
     */
    public void startApplication(String petId, String petName) {
        final SubmitApplicationState state = submitApplicationViewModel.getState();
        state.setPetId(petId);
        state.setPetName(petName);
        state.setError("");
        state.setSuccessMessage("");
        submitApplicationViewModel.notifyStateChanged();
        viewManagerModel.setActiveView(SubmitApplicationViewModel.VIEW_NAME);
    }

    /**
     * Opens My Applications.
     */
    public void showMyApplications() {
        viewManagerModel.setActiveView(TrackApplicationsViewModel.VIEW_NAME);
    }
}

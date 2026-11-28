package interface_adapter.submit_application;

import use_case.submit_application.SubmitApplicationOutputBoundary;
import use_case.submit_application.SubmitApplicationOutputData;

/**
 * The Presenter for the Submit Application Use Case.
 */
public class SubmitApplicationPresenter implements SubmitApplicationOutputBoundary {

    private final SubmitApplicationViewModel submitApplicationViewModel;

    /**
     * Creates the presenter.
     * @param submitApplicationViewModel the view model to update
     */
    public SubmitApplicationPresenter(SubmitApplicationViewModel submitApplicationViewModel) {
        this.submitApplicationViewModel = submitApplicationViewModel;
    }

    @Override
    public void prepareSuccessView(SubmitApplicationOutputData outputData) {
        final SubmitApplicationState state = submitApplicationViewModel.getState();
        state.setError("");
        state.setSuccessMessage("Your application for " + outputData.getPetName()
                + " was submitted. You can follow it in My Applications.");
        submitApplicationViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SubmitApplicationState state = submitApplicationViewModel.getState();
        state.setSuccessMessage("");
        state.setError(errorMessage);
        submitApplicationViewModel.notifyStateChanged();
    }
}

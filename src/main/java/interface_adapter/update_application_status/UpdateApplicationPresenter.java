package interface_adapter.update_application_status;

import interface_adapter.ViewManagerModel;
import use_case.update_application_status.UpdateApplicationOutputBoundary;
import use_case.update_application_status.UpdateApplicationOutputData;

public class UpdateApplicationPresenter implements UpdateApplicationOutputBoundary {
    private final UpdateApplicationViewModel updateApplicationViewModel;
    private final ViewManagerModel viewManagerModel;

    public UpdateApplicationPresenter(UpdateApplicationViewModel updateApplicationViewModel,
                                      ViewManagerModel viewManagerModel) {
        this.updateApplicationViewModel = updateApplicationViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(UpdateApplicationOutputData outputData) {
        final UpdateApplicationState updateApplicationState = updateApplicationViewModel.getState();
        updateApplicationState.setUpdateError("");
        updateApplicationState.setNewStatus(outputData.getApplicationStatus());
        updateApplicationState.setAdminNote(outputData.getAdminNote());
        updateApplicationViewModel.notifyStateChanged();

        viewManagerModel.setActiveView(UpdateApplicationViewModel.VIEW_NAME);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final UpdateApplicationState updateApplicationState = updateApplicationViewModel.getState();
        updateApplicationState.setUpdateError(errorMessage);
        updateApplicationViewModel.notifyStateChanged();

    }
}

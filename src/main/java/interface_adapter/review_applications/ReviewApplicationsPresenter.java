package interface_adapter.review_applications;

import entity.ApplicationStatus;
import interface_adapter.ViewManagerModel;
import interface_adapter.update_application_status.UpdateApplicationState;
import interface_adapter.update_application_status.UpdateApplicationViewModel;
import use_case.review_applications.ApplicationDetailsOutputData;
import use_case.review_applications.ReviewApplicationsOutputBoundary;
import use_case.review_applications.ReviewApplicationsOutputData;

/**
 * The Presenter for the Review Applications Use Case.
 */
public class ReviewApplicationsPresenter implements ReviewApplicationsOutputBoundary {

    private final ReviewApplicationsViewModel reviewApplicationsViewModel;
    private final UpdateApplicationViewModel updateApplicationViewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Creates the presenter.
     * @param reviewApplicationsViewModel the view model to update
     */
    public ReviewApplicationsPresenter(ReviewApplicationsViewModel reviewApplicationsViewModel,
                                       UpdateApplicationViewModel updateApplicationViewModel,
                                       ViewManagerModel viewManagerModel) {
        this.reviewApplicationsViewModel = reviewApplicationsViewModel;
        this.updateApplicationViewModel = updateApplicationViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ReviewApplicationsOutputData outputData) {
        final ReviewApplicationsState state = reviewApplicationsViewModel.getState();
        state.clearApplications();
        for (int i = 0; i < outputData.getApplicationCount(); i++) {
            state.addApplication(outputData.getApplicationIds().get(i), outputData.getPetNames().get(i),
                    outputData.getAdopterNames().get(i), outputData.getSubmittedDates().get(i).toString(),
                    outputData.getStatuses().get(i));
        }
        state.setMessage("Select an application to read the adopter's answers.");
        // The list changed, so the opened application may no longer be in it.
        state.clearDetails();
        state.setDetailMessage("");
        reviewApplicationsViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final ReviewApplicationsState state = reviewApplicationsViewModel.getState();
        state.clearApplications();
        state.setMessage(errorMessage);
        state.clearDetails();
        state.setDetailMessage("");
        reviewApplicationsViewModel.notifyStateChanged();
    }

    @Override
    public void prepareDetailView(ApplicationDetailsOutputData outputData) {
        final ReviewApplicationsState state = reviewApplicationsViewModel.getState();
        state.setDetailApplicationId(outputData.getApplicationId());
        state.setDetailPetName(outputData.getPetName());
        state.setDetailAdopterName(outputData.getAdopterName());
        state.setDetailSubmittedAt(outputData.getSubmittedAt().toString());
        state.setDetailStatus(outputData.getStatus());
        state.setDetailHousingType(outputData.getHousingType());
        state.setDetailPetExperience(outputData.getPetExperience());
        state.setDetailHouseholdMembers(outputData.getHouseholdMembers());
        state.setDetailContactInfo(outputData.getContactInfo());
        state.setDetailNote(orPlaceholder(outputData.getNote(), "(no note from the adopter)"));
        state.setDetailAdminNote(orPlaceholder(outputData.getAdminNote(), "(no note from the shelter yet)"));
        state.setDetailMessage("");
        reviewApplicationsViewModel.notifyStateChanged();
    }

    @Override
    public void prepareDetailFailView(String errorMessage) {
        final ReviewApplicationsState state = reviewApplicationsViewModel.getState();
        state.clearDetails();
        state.setDetailMessage(errorMessage);
        reviewApplicationsViewModel.notifyStateChanged();
    }

    private static String orPlaceholder(String text, String placeholder) {
        if (text == null || text.isEmpty()) {
            return placeholder;
        }
        return text;
    }

    @Override
    public void switchToUpdateApplicationView() {
        final ReviewApplicationsState reviewApplicationsState = reviewApplicationsViewModel.getState();
        final UpdateApplicationState updateApplicationState = updateApplicationViewModel.getState();

        updateApplicationState.setApplicationId(reviewApplicationsState.getDetailApplicationId());
        updateApplicationState.setAdopterName(reviewApplicationsState.getDetailAdopterName());
        updateApplicationState.setPetName(reviewApplicationsState.getDetailPetName());
        updateApplicationState.setAdminNote(reviewApplicationsState.getDetailAdminNote());
        updateApplicationState.setUpdateError("");

        updateApplicationViewModel.notifyStateChanged();
        viewManagerModel.setActiveView(updateApplicationViewModel.VIEW_NAME);

    }
}

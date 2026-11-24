package interface_adapter.review_applications;

import use_case.review_applications.ApplicationDetailsOutputData;
import use_case.review_applications.ReviewApplicationsOutputBoundary;
import use_case.review_applications.ReviewApplicationsOutputData;

public class ReviewApplicationsPresenter implements ReviewApplicationsOutputBoundary {
    private final ReviewApplicationsViewModel reviewApplicationsViewModel;

    public ReviewApplicationsPresenter(ReviewApplicationsViewModel reviewApplicationsViewModel) {
        this.reviewApplicationsViewModel = reviewApplicationsViewModel;
    }

    @Override
    public void prepareSuccessView(ReviewApplicationsOutputData reviewApplicationsOutputData) {

    }

    @Override
    public void prepareDetailView(ApplicationDetailsOutputData applicationDetailsOutputData) {

    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}

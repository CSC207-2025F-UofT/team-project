package use_case.review_applications;

public class ReviewApplicationsInteractor {

    private final ReviewApplicationsOutputBoundary reviewApplicationsPresenter;

    public ReviewApplicationsInteractor(ReviewApplicationsOutputBoundary reviewApplicationsPresenter) {
        this.reviewApplicationsPresenter = reviewApplicationsPresenter;
    }
}

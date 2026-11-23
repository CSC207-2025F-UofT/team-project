package use_case.review_applications;

public interface ReviewApplicationsOutputBoundary {

    void prepareSuccessView(ReviewApplicationsOutputData reviewApplicationsOutputData);

    void prepareFailView(String errorMessage);
}

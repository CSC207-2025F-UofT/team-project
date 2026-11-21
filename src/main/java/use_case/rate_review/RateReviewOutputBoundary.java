package use_case.rate_review;

public interface RateReviewOutputBoundary {
    void prepareSuccessView(RateReviewOutputData rateReviewOutputData);
    void prepareFailView(String errorMessage);
}

package use_case.rate_review;

public interface RateReviewOutputBoundary {
    /**
     * Prepares the success view for the Rate Review Book use case
     * @param rateReviewOutputData the output data
     */
    void prepareSuccessView(RateReviewOutputData rateReviewOutputData);
    /**
     * Prepares the fail view for the Rate Review Book use case
     * @param errorMessage the explanation for the failure
     */
    void prepareFailView(String errorMessage);
}

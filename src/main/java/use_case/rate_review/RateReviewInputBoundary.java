package use_case.rate_review;
/**
 * Input Boundary for actions which are related to adding review and rating of book.
 */
public interface RateReviewInputBoundary {
    /**
     * Executes the rate and review use case.
     * @param rateReviewInputData the input data
     */
    void execute(RateReviewInputData rateReviewInputData);
}

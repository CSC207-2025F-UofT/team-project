package use_case.rate_review;
/**
 * Input Boundary for actions which are related to adding user review and rating of book.
 */
public interface RateReviewInputBoundary {
    /**
     * Executes the review/rate use case.
     * @param rateReviewInputData the input data
     */
    void execute(RateReviewInputData rateReviewInputData);
}

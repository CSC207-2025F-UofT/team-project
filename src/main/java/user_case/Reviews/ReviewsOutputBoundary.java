package user_case.Reviews;

/**
 * output boundary for the Reviews case
 */


public interface ReviewsOutputBoundary {
    void prepareSuccessView(ReviewsOutputData outputData);
    void prepareFailView(String errorMessage);
}

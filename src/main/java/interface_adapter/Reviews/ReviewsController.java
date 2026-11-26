package interface_adapter.Reviews;

import user_case.Reviews.ReviewsInputBoundary;
import user_case.Reviews.ReviewsInputData;

public class ReviewsController {
    private final ReviewsInputBoundary inputBoundary;

    public ReviewsController(ReviewsInputBoundary inputBoundary){
        this.inputBoundary = inputBoundary;
    }
    public void execute(int movieId){
        ReviewsInputData inputData = new ReviewsInputData(movieId);
        inputBoundary.execute(inputData);
    }
}

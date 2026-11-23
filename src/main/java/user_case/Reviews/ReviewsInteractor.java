package user_case.Reviews;

import entity.MovieReview;
import java.util.List;
import java.io.IOException;

public class ReviewsInteractor implements ReviewsInputBoundary{
    private final ReviewsDataAccessInterface dataAccess;
    private final ReviewsOutputBoundary outputBoundary;

    public ReviewsInteractor(ReviewsDataAccessInterface dataAccess, ReviewsOutputBoundary outputBoundary){
        this.dataAccess = dataAccess;
        this.outputBoundary  = outputBoundary;
    }
    @Override
    public void execute(ReviewsInputData inputData){
        int movieId = inputData.getMovieId();
        try {
            List<MovieReview> reviews = dataAccess.getReviews(movieId);
            outputBoundary.prepareSuccessView(new ReviewsOutputData(reviews));
        } catch (IOException e){
            outputBoundary.prepareFailView("Failed to retrieve reviews");
        }
    }

}

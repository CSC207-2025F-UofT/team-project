package interface_adapter.Reviews;

import user_case.Reviews.ReviewsOutputBoundary;
import user_case.Reviews.ReviewsOutputData;
import entity.MovieReview;

import java.util.ArrayList;
import java.util.List;

public class ReviewPresenter implements ReviewsOutputBoundary {
    private final ReviewsViewModel viewModel;

    public ReviewPresenter(ReviewsViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ReviewsOutputData outputData){
        List<String> formattedReviews = new ArrayList<>();
        for(MovieReview review : outputData.getReviews()){
            String ratingText;
            if(review.getRating() == null) {
                ratingText = "No rating";
            } else {
                ratingText = review.getRating().toString();
            }

            String formattedReview = review.getAuthor() + "(" + ratingText +"\n" + review.getContent();
            formattedReviews.add(formattedReview);
        }
        ReviewsState state = new ReviewsState();
        state.setReviews(formattedReviews);

        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage){
        ReviewsState state = new ReviewsState();
        state.setErrorMessages(errorMessage);
    }
}

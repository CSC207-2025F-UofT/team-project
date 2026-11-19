package interface_adapter.Reviews;

import user_case.Reviews.ReviewsOutputBoundary;
import user_case.Reviews.ReviewsOutputData;

import java.util.ArrayList;

public class ReviewPresenter implements ReviewsOutputBoundary {
    private final ReviewsViewModel viewModel;

    public ReviewPresenter(ReviewsViewModel viewModel){
        this.viewModel = viewModel;
    }
    @Override
    public void prepareSuccessView(ReviewsOutputData outputData){
        viewModel.setErrorMessage("");
        viewModel.setReviews(outputData.getReviews());
    }
    @Override
    public void prepareFailView(String errorMessage){
        viewModel.setErrorMessage(errorMessage);
        viewModel.setReviews(new ArrayList<>());
    }
}

package interface_adapter.Reviews;

import entity.MovieReview;
import java.util.ArrayList;
import java.util.List;

public class ReviewsViewModel {
    private List<MovieReview> reviews = new ArrayList<>();
    private String errorMessage = "";

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
